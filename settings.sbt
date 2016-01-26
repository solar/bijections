organization := "org.sazabi"

crossScalaVersions := Seq("2.11.7", "2.10.6")

scalaVersion := crossScalaVersions.value.head

libraryDependencies += "com.twitter" %% "bijection-core" % "0.8.1"

libraryDependencies += "com.github.scalaprops" %% "scalaprops" % "0.1.16" % "test"

testFrameworks += new TestFramework("scalaprops.ScalapropsFramework")

releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseCrossBuild := true

parallelExecution in Global := false

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/solar/bijections</url>
  <licenses>
    <license>
      <name>Apache 2</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt"</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:solar/bijections.git</url>
    <connection>scm:git:git@github.com:solar/bijections.git</connection>
  </scm>
  <developers>
    <developer>
      <id>solar</id>
      <name>Shinpei Okamura</name>
      <url>https://github.com/solar</url>
    </developer>
  </developers>)
