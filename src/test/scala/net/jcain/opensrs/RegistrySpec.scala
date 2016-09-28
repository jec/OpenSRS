/*
 * OpenSRS -- a Scala library for using the OpenSRS API
 * Copyright (C) 2016 James Edwin Cain (user opensrs, domain jcain.net)
 *
 * This file is part of the net.jcain.opensrs library.  This Library is free
 * software; you may redistribute it or modify it under the terms of the
 * license contained in the file LICENCE.txt. If you did not receive a copy of
 * the license, please contact the copyright holder.
 */

package net.jcain.opensrs

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit, TestProbe}
//import com.typesafe.config.ConfigFactory
import org.scalatest.{Matchers, WordSpecLike}
import concurrent.duration._

object RegistrySpec {

  val User = Option(System.getProperty("user")) match {
    case Some(s) => s
    case None => throw new RuntimeException("Please set the OpenSRS username on the command line: -Duser=myuser")
  }

  val PrivateKey = Option(System.getProperty("key")) match {
    case Some(s) => s
    case None => throw new RuntimeException("Please set the OpenSRS private key on the command line: -Dkey=mykey")
  }

  val Hostname = "horizon.opensrs.net"

}

class RegistrySpec extends TestKit(ActorSystem("RegistrySpec")) with WordSpecLike with Matchers {

  import RegistrySpec._

  class RegistryFixture(label: String, key: Option[String] = None) {

    val testProbe = TestProbe(s"$label-probe")
    val registry = TestActorRef(Props(classOf[Registry], User, key.getOrElse(PrivateKey), Some(Hostname), None, Some(testProbe.ref)), s"$label-registry")

    def stop() = {
      system.stop(registry)
    }

  }

  "Registry" when {

    "md5Digest()" should {
      "return a valid MD5 hex digest" in {
        Registry.md5Digest("<test/>") shouldBe "f1430934c390c118ed2f148e1d44d36c"
        // from the OpenSRS guide
        Registry.md5Digest("ConnecttoOpenSRSviaSSL") shouldBe "e787cc1d1951dfec4827cede7b1a0933"
      }
    }

    "sign()" should {
      "return a valid signature" in {
        Registry.sign("<test/>", "key") shouldBe "48ed6e2bb03a40a5c9a7d4b5957d7663"
      }
    }

    "uses an invalid user/key" should {
      "respond with AuthenticationFailed" in new RegistryFixture("authfail", Some("badkey")) {
        registry ! request.domain.LookUp("example.com")
        testProbe.expectMsgPF(30.seconds) { case response.AuthenticationFailed(_) => }
        stop()
      }
    }

    "receives a Request" should {
      "respond with the Response" in new RegistryFixture("lookup") {
        registry ! request.domain.LookUp("example.com")
        testProbe.expectMsgPF(30.seconds) {
          case response.BadRequest(e) => fail(e)
          case response.AuthenticationFailed(e) => fail(e)
          case x: Any => println(s"Got response: $x")
        }
        stop()
      }
    }

  }

}
