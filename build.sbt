name := "RIS"

version := "1.0"

organization := "de.hci"

scalaVersion := "2.10.3"

autoScalaLibrary := true

autoCompilerPlugins := true

retrieveManaged := true

scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-deprecation",
    "-unchecked"
)

scalaSource in Compile <<= baseDirectory(_ / "src")

javacOptions ++= Seq(
    "-encoding",
    "UTF-8"
)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % "latest.integration" withSources() withJavadoc(),
  "com.typesafe.akka" %% "akka-agent"   % "latest.integration" withSources() withJavadoc()
)

transitiveClassifiers := Seq(
	"sources",
	"javadoc"
)

unmanagedJars <<= baseDirectory map { base => ((base ** "lib") ** "*.jar").classpath }