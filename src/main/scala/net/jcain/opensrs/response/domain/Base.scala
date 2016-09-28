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

import scala.xml.Elem

object Base {

  def create(elem: Elem): Option[Base] = {
    val items = elem \ "body" \ "data_block" \ "dt_assoc" \ "item"
    (for { item <- items if (item \ "@key").text == "object" } yield item.text).headOption match {
      case Some("DOMAIN") =>
        (for { item <- items if (item \ "@key").text == "response_text" } yield item.text).headOption.map(Base(_))
      case Some(_) => None
      case None => None
    }
  }

}

case class Base(text: String) extends net.jcain.opensrs.response.Base {

}
