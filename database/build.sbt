name := """koalifyDB"""

// Would it be worth it to share this between top level and this one?
// May not be a huge deal because this only runs migrations.
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "9.4.1212"
)

flywayLocations := Seq("filesystem:migrations")
