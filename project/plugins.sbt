resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

resolvers += "Sonatype Bremen" at "http://www.atb-bremen.de/artifactory/list/sonatype/"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.7.0-SNAPSHOT")

addSbtPlugin("com.github.philcali" % "sbt-lwjgl-plugin" % "3.1.5")