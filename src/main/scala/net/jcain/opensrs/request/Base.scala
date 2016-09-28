/*
 * OpenSRS -- a Scala library for using the OpenSRS API
 * Copyright (C) 2016 James Edwin Cain (user opensrs, domain jcain.net)
 *
 * This file is part of the net.jcain.opensrs library.  This Library is free
 * software; you may redistribute it or modify it under the terms of the
 * license contained in the file LICENCE.txt. If you did not receive a copy of
 * the license, please contact the copyright holder.
 */

package net.jcain.opensrs.request

abstract class Base {

  def action: String

  def objectName: String

  def attributes: scala.xml.Elem

  //def encode(indent: Int = 0, step: Int = 2): scala.xml.Elem

  def toXml = {
    val writer = new java.io.StringWriter
    val node = <OPS_envelope>
      <header>
          <version>0.9</version>
      </header>
      <body>
        <data_block>
          <dt_assoc>
            <item key="protocol">XCP</item>
            <item key="action">{action}</item>
            <item key="object">{objectName}</item>
            <item key="attributes">
              {attributes}
            </item>
          </dt_assoc>
        </data_block>
      </body>
    </OPS_envelope>
    xml.XML.write(writer, node, "UTF-8", xmlDecl = true, xml.dtd.DocType("OPS_envelope", xml.dtd.SystemID("ops.dtd"), Nil))
    writer.toString
  }

}
