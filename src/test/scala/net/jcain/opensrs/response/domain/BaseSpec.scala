/*
 * OpenSRS -- a Scala library for using the OpenSRS API
 * Copyright (C) 2016 James Edwin Cain (user opensrs, domain jcain.net)
 *
 * This file is part of the net.jcain.opensrs library.  This Library is free
 * software; you may redistribute it or modify it under the terms of the
 * license contained in the file LICENCE.txt. If you did not receive a copy of
 * the license, please contact the copyright holder.
 */

package net.jcain.opensrs.response.domain

import org.scalatest.{Matchers, WordSpec}

class BaseSpec extends WordSpec with Matchers {

  "BaseSpec" when {

    "given DOMAIN/REPLY XML" should {
      "return Some(Base)" in {
        val xml = {
          <OPS_envelope>
            <header>
              <version>0.9</version>
            </header>
            <body>
              <data_block>
                <dt_assoc>
                  <item key="protocol">XCP</item>
                  <item key="object">DOMAIN</item>
                  <item key="response_text">Domain taken</item>
                  <item key="action">REPLY</item>
                  <item key="attributes">
                    <dt_assoc>
                      <item key="status">taken</item>
                    </dt_assoc>
                  </item>
                  <item key="response_code">211</item>
                  <item key="is_success">1</item>
                </dt_assoc>
              </data_block>
            </body>
          </OPS_envelope>
        }
        Base.create(xml) shouldBe Some(Base("Domain taken"))
      }
    }

  }

}
