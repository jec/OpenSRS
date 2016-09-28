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

import xml._

object BadRequest {

  def create(elem: Elem): Option[BadRequest] = {
    val items = elem \ "body" \ "data_block" \ "dt_assoc" \ "item"
    val code = for { item <- items if (item \ "@key").text == "response_code" } yield item.text
    if (code.head == "404") {
      val error = for { item <- items if (item \ "@key").text == "response_text" } yield item.text
      Some(BadRequest(error.head))
    }
    else None
  }

}

case class BadRequest(error: String) extends Base
