organization := "com.example"
name := "finch_example"
version := "0.0.1-SNAPSHOT"
scalaVersion := "2.12.5"

libraryDependencies ++= {
  Seq(
    "com.github.finagle" %% "finch-core" % "0.17.0",
    "com.github.finagle" %% "finch-circe" % "0.17.0",
    "io.circe" %% "circe-generic" % "0.9.0",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )
}

lazy val root = (project in file("."))
