
def commonSettings = Seq(
  organization := "io.growing",
  version := "1.0",
  scalaVersion := "2.11.8"
)

lazy val playless = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "playless",
    libraryDependencies += component("play-server")
  )

lazy val sample = (project in file("sample"))
  .dependsOn(playless)
  .settings(commonSettings: _*)
  .settings(
    name := "sample",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % "10.0.1",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.1",
      "com.h2database" % "h2" % "1.4.187",
      "com.typesafe.play" %% "play-slick-evolutions" % "2.0.2",
      "com.typesafe.play" %% "play-slick" % "2.0.2"
    )
  )
    