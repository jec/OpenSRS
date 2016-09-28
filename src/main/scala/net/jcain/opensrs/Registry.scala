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

import akka.actor.{Actor, ActorRef}
import akka.http.scaladsl.{ConnectionContext, Http}
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.RawHeader
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import javax.net.ssl.SSLContext

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

object Registry {

  val DefaultHostname = "rr-n1-tor.opensrs.net"
  val DefaultPort = 55443

  val md5 = java.security.MessageDigest.getInstance("MD5")

  def md5Digest(str: String): String =
    md5.digest(str.getBytes).map("%02x".format(_)).mkString

  def sign(str: String, privateKey: String): String =
    md5Digest(md5Digest(str + privateKey) + privateKey)

}

class Registry(
  user: String,
  privateKey: String,
  _hostname: Option[String] = None,
  _port: Option[Int] = None,
  _parent: Option[ActorRef] = None
) extends Actor {

  import Registry._
  import akka.pattern.pipe

  protected val log = org.slf4j.LoggerFactory.getLogger(this.getClass)

  final implicit val materializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  val hostname = _hostname.getOrElse(DefaultHostname)
  val port = _port.getOrElse(DefaultPort)
  val privateKeyBytes = privateKey.getBytes
  log.debug(s"Connecting to OpenSRS as $user on $hostname:$port")

  // set up Http
  val sslContext: SSLContext = {
    val sc = SSLContext.getInstance("TLSv1.2")
    sc.init(null, null, null)
    sc
  }
  val http = Http(context.system)
  http.setDefaultClientHttpsContext(ConnectionContext.https(sslContext))

  // allow override of parent
  val parent = _parent.getOrElse(context.parent)

  def receive = {

    case request: request.Base =>
      val xmlString = request.toXml
      val httpRequest = HttpRequest(
        HttpMethods.POST,
        s"https://$hostname:$port/",
        List(
          RawHeader("X-Username", user),
          RawHeader("X-Signature", sign(xmlString, privateKey))
        ),
        HttpEntity(ContentTypes.`text/xml(UTF-8)`, xmlString)
      )
      println(httpRequest)
      http.singleRequest(httpRequest).pipeTo(self)

    case HttpResponse(StatusCodes.OK, _, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).map(_.utf8String).map(s => Try(response.Base(s))).map {
        //case Success(Right(n @ None)) => println("0: n"); n
        case Success(Right(r)) => println(s"1: $r"); r
        case Success(Left(e)) => println("2:"); e.printStackTrace(); e
        case Failure(e) => println(s"3: $e"); e // TODO: wrap in a response.RequestFailed
        case x: Any => println(s"4: $x"); x
      }.pipeTo(parent)

    case HttpResponse(code, _, entity, _) =>
      log.info("Request failed, response code: " + code)

    case Failure(error) =>
      log.error(s"Request failed: $error")
      parent ! error // TODO: wrap in a response.RequestFailed

    case x =>
      println(s"Received $x (${x.getClass.getName})")
  }

}
