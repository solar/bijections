import AddSettings._

def proj(name: String) = Project(s"bijections-$name", file(name)).settingSets(
  autoPlugins, buildScalaFiles, userSettings, sbtFiles(file("../settings.sbt")))

lazy val root = project.in(file(".")).settingSets(
  allPlugins, buildScalaFiles, userSettings, defaultSbtFiles
).settings(
  publishArtifact := false
).aggregate(core, argonaut, joda, scalaz)

lazy val core = proj("core").settings(
  name := "bijections-core"
)

lazy val argonaut = proj("argonaut").settings (
  libraryDependencies += "io.argonaut" %% "argonaut" % "6.1"
)

lazy val joda = proj("jodatime").settings (
  libraryDependencies ++= Seq(
    "com.github.nscala-time" %% "nscala-time" % "2.6.0",
    "org.scalaz" %% "scalaz-core" % "7.1.6",
    "com.chuusai" %% "shapeless" % "2.2.5"),
  libraryDependencies ++= (
    if (scalaVersion.value.startsWith("2.10"))
      Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full))
    else Seq()
  )
)

lazy val scalaz = proj("scalaz").settings(
  libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.1.6"
)
