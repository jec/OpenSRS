/*
 * OpenSRS -- a Scala library for using the OpenSRS API
 * Copyright (C) 2016 James Edwin Cain (user opensrs, domain jcain.net)
 *
 * This file is part of the net.jcain.opensrs library.  This Library is free
 * software; you may redistribute it or modify it under the terms of the
 * license contained in the file LICENCE.txt. If you did not receive a copy of
 * the license, please contact the copyright holder.
 */

name := "opensrs"

organization := "net.jcain"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.8"

javaHome := Some(file("/usr/java/latest"))

libraryDependencies ++= {
  val akkaVersion = "2.4.10"
  Seq(
    "com.typesafe.akka"   %% "akka-http-core"   % akkaVersion,
    "com.typesafe.akka"   %% "akka-http-testkit" % akkaVersion  % "test",
    "ch.qos.logback"      %  "logback-classic"  % "1.1.7",
    "com.github.scopt"    %% "scopt"            % "3.5.0",
    "org.scala-lang.modules" %% "scala-xml"     % "1.0.6",
    "org.scalatest"       %% "scalatest"        % "3.0.0"       % "test"
  )
}

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  //"-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  //"-Ywarn-value-discard",
  "-Xfuture",
  "-Ywarn-unused-import"
)
