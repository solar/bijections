import AddSettings._

val f = file("../settings.sbt")

def proj(name: String) =
  Project(s"bijections-$name", file(name)).settingSets(
    autoPlugins, buildScalaFiles, userSettings, sbtFiles(f))

lazy val root = project.in(file(".")).settingSets(
  allPlugins, buildScalaFiles, userSettings, defaultSbtFiles
).settings(
  publishArtifact := false
).aggregate(core, argonaut, joda, scalaz)

lazy val core = proj("core").settings(
  name := "bijections-core"
)

lazy val argonaut = proj("argonaut").settings (
  libraryDependencies += "io.argonaut" %% "argonaut" % "6.1-M6"
)

lazy val joda = proj("jodatime").settings (
  libraryDependencies ++= Seq(
    "joda-time" % "joda-time" % "2.7",
    "org.joda" % "joda-convert" % "1.7" % "provided",
    "org.scalaz" %% "scalaz-core" % "7.1.1")
)

lazy val scalaz = proj("scalaz").settings(
  libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.1"
)
