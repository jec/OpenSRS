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

class BadRequestSpec extends WordSpec with Matchers {

  "BadRequestSpec" when {

    "given matching XML" should {
      "return Some(BadRequest)" in {
        val xml = {
          <OPS_envelope>
            <header>
              <version>0.9</version>
            </header>
            <body>
              <data_block>
                <dt_assoc>
                  <item key="protocol">XCP</item>
                  <item key="response_text">Missing X-Username: header</item>
                  <item key="response_code">404</item>
                  <item key="is_success">1</item>
                </dt_assoc>
              </data_block>
            </body>
          </OPS_envelope>
        }
        BadRequest.create(xml) match {
          case None =>
            fail("Should not be None")
          case Some(BadRequest(message)) =>
            message shouldBe "Missing X-Username: header"
        }
      }
    }

    "given non-matching XML" should {
      "return None" in {
        val xml = {
          <OPS_envelope>
            <header>
              <version>0.9</version>
            </header>
            <body>
              <data_block>
                <dt_assoc>
                  <item key="protocol">XCP</item>
                  <item key="response_text">OK</item>
                  <item key="response_code">200</item>
                  <item key="is_success">1</item>
                </dt_assoc>
              </data_block>
            </body>
          </OPS_envelope>
        }
        BadRequest.create(xml) shouldBe None
      }
    }

  }

}
