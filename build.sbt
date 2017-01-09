
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

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Some("snapshots" at nexus + "content/repositories/snapshots")
  }
  else {
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}

publishMavenStyle := true

pomExtra :=
  <url>https://github.com/scweang/playless</url>
    <licenses>
      <license>
        <name>Apache License</name>
        <url>http://www.apache.org/licenses/</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:scweang/playless.git</url>
      <connection>scm:git:git@github.com:scweang/playless.git</connection>
    </scm>
    <developers>
      <developer>
        <id>scwang</id>
        <name>Wayne Wang</name>
        <url>http://scweang.me/</url>
      </developer>
    </developers>