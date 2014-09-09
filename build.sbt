name := "eventsource_workshop"

scalaVersion := "2.11.2"

val versionAkka = "2.3.6"
val akkaLibs = Seq(
  "com.typesafe.akka" %% "akka-actor" % versionAkka,
  "com.typesafe.akka" %% "akka-persistence-experimental" % versionAkka
)

val testLibs = Seq(
  "com.typesafe.akka" %% "akka-testkit" % versionAkka % "test",
  "org.scalatest" %% "scalatest" % "2.2.2" % "test"
)

val versionUnfiltered = "0.8.1"
val unfiltered = Seq(
  "net.databinder" %% "unfiltered-filter" % versionUnfiltered,
  "net.databinder" %% "unfiltered-jetty" % versionUnfiltered
)

libraryDependencies ++= akkaLibs ++ testLibs ++ unfilter
ed
