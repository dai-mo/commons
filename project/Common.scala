
import sbt._
import Keys._


import Dependencies._


object Common {
  lazy val commonSettings = Seq(
    organization := "org.dcs",
    scalaVersion := scVersion,
    crossPaths := false,
    checksums in update := Nil,
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked"),
    javacOptions in doc := Seq("-source", "1.8")    
    )
}