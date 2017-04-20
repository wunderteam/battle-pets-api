name := """pet-api"""

scalaVersion := "2.11.8"

version := "1.0-SNAPSHOT"

enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  cache,
  filters,
  ws,
  "com.typesafe.play" %% "play-slick" % "2.0.2",
  "org.postgresql" % "postgresql" % "9.4.1212",
  "org.typelevel" %% "cats" % "0.9.0",
  "eu.timepit" %% "refined" % "0.8.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

// commented out because it causes issues for SBT.
// error:
// fork in run := true

// Make sure that non-exhaustive matches fail compilation.
// This will allow you to leverage the type system to assure
// That errors are handled in controllers. See apps/contests/app/ContestsController
scalacOptions += "-Xfatal-warnings"

// Use the test database.
javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

wartremoverErrors ++= Warts.unsafe
// See: https://github.com/wartremover/wartremover/issues/112
wartremoverExcluded += crossTarget.value / "routes" / "main" / "router" / "Routes.scala"
wartremoverExcluded += crossTarget.value / "routes" / "main" / "router" / "RoutesPrefix.scala"
// The config is pretty harmless so just ignore it.
wartremoverExcluded += baseDirectory.value / "app" / "web" / "DIConfiguration.scala"

clippyColorsEnabled := true
