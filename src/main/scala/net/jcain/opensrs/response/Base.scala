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

import scala.util.{Failure, Success, Try}
import scala.xml.SAXParser
import scala.xml.factory.XMLLoader

object Base {

  // from http://stackoverflow.com/questions/31841399
  object NonValidatingXmlLoader extends XMLLoader[xml.Elem] {
    override def parser: SAXParser = {
      val f = javax.xml.parsers.SAXParserFactory.newInstance()
      f.setValidating(false)
      f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
      f.newSAXParser()
    }
  }

  def apply(xmlString: String): Either[Throwable, _ >: Base] = {
    println(xmlString)
    Try(NonValidatingXmlLoader.loadString(xmlString)) match {
      case Failure(error) => Left(error)
      case Success(elem) =>
        val result = AuthenticationFailed.create(elem).orElse(BadRequest.create(elem)).getOrElse(Unknown.create(elem))
        Right(result)
    }
  }

}

abstract class Base {

}
