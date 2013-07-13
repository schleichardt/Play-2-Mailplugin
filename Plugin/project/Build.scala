import sbt._
import Keys._
import play.Project._

object MailerBuild extends Build {

  lazy val moduleVersion =  "0.9.1"
  lazy val moduleOrganization = "info.schleichardt"
  lazy val moduleName = "Play-2-Mailplugin"

  val moduleDependencies = Seq(
       "org.apache.commons" % "commons-email" % "1.3.1"
    , "junit" % "junit-dep" % "4.11" % "test"
  )

  lazy val githubPath = "schleichardt/Play-2-Mailplugin"

  lazy val main = play.Project(moduleName, moduleVersion, moduleDependencies).settings(
    organization := moduleOrganization,
    playPlugin := true,
    javacOptions ++= Seq("-source", "1.6", "-target", "1.6"),//for compatibility with Debian Squeeze
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <url>https://github.com/{githubPath}</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <url>git@github.com:{githubPath}.git</url>
          <connection>scm:git:git@github.com:{githubPath}.git</connection>
        </scm>
        <developers>
          <developer>
            <id>schleichardt</id>
            <name>Michael Schleichardt</name>
            <url>http://michael.schleichardt.info</url>
          </developer>
        </developers>
      )
  )
}