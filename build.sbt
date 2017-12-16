name := "Final_Project"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

val scalaTestVersion = "2.2.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  //"org.scalatest" % "scalatest_2.10" % "2.2.1",
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-sql" % "2.2.0",
 // "org.apache.spark" %% "spark-streaming1" % "2.2.0",
  "org.apache.spark" %% "spark-mllib" % "2.2.0"
//  ExclusionRule(organization = "org.scalacheck"),
//  ExclusionRule(organization = "org.scalactic"),
//  ExclusionRule(organization = "org.scalatest")
)


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % "test"
