name := "Final_Project"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

val scalaTestVersion = "2.2.4"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0",
  "org.apache.spark" %% "spark-sql" % "2.2.0",
 // "org.apache.spark" %% "spark-streaming1" % "2.2.0",
  "org.apache.spark" %% "spark-mllib" % "2.2.0"
)

