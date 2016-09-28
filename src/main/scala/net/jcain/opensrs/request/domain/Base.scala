/*
 * OpenSRS -- a Scala library for using the OpenSRS API
 * Copyright (C) 2016 James Edwin Cain (user opensrs, domain jcain.net)
 *
 * This file is part of the net.jcain.opensrs library.  This Library is free
 * software; you may redistribute it or modify it under the terms of the
 * license contained in the file LICENCE.txt. If you did not receive a copy of
 * the license, please contact the copyright holder.
 */

package net.jcain.opensrs.request.domain

abstract class Base extends net.jcain.opensrs.request.Base {

  val domain: String

  def objectName = "DOMAIN"

  def attributes =
    <dt_assoc>
      <item key="domain">{domain}</item>
    </dt_assoc>

}
