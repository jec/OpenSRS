/*
 * OpenSRS -- a Scala library for using the OpenSRS API
 * Copyright (C) 2016 James Edwin Cain (user opensrs, domain jcain.net)
 *
 * This file is part of the net.jcain.opensrs library.  This Library is free
 * software; you may redistribute it or modify it under the terms of the
 * license contained in the file LICENCE.txt. If you did not receive a copy of
 * the license, please contact the copyright holder.
 */

package net.jcain.opensrs.response

import org.scalatest.{Matchers, WordSpec}

class BaseSpec extends WordSpec with Matchers {

  "Base" when {
    "apply()" when {
      "given invalid XML" should {
        "return Left" in {
          Base("badxml") should matchPattern { case Left(_) => }
        }
      }
      "given valid XML" should {
        "return Right" in {
          Base("<tag></tag>") should matchPattern { case Right(_) => }
        }
      }
    }
  }

}
