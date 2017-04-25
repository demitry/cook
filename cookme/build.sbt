name := """cookme"""
organization := "DMLab"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.8"

libraryDependencies += filters

libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"

libraryDependencies ++= Seq(
  jdbc, javaJpa, cache, evolutions,
  "mysql" % "mysql-connector-java" % "5.1.28"
)

lazy val myProject = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
