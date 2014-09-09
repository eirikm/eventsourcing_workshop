name := "eventsource_workshop"

scalaVersion := "2.11.2"

val versionAkka = "2.3.5"

val akkaDeps = Seq(
  "com.typesafe.akka" %% "akka-actor" % versionAkka,
  "com.typesafe.akka" %% "akka-persistence-experimental" % versionAkka
)

val testLibs = Seq(
  "com.typesafe.akka" % "akka-testkit" % versionAkka % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.2" % "test"
)

libraryDependencies ++= akkaDeps ++ testLibs
