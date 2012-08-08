import sbt._
import Keys._

object MailerBuild extends Build {

  lazy val moduleVersion =  "0.6.1"
  lazy val moduleOrganization = "info.schleichardt"
  lazy val moduleName = "Play-2-Mailplugin"
  lazy val publishingFolder = Path.userHome.absolutePath+"/Projekte/schleichardt.github.com/jvmrepo"

  val moduleDependencies = Seq(
       "org.apache.commons" % "commons-email" % "1.2"
  )

  lazy val main = PlayProject(moduleName, moduleVersion, moduleDependencies).settings(
      organization := moduleOrganization,
      publishTo := Some(Resolver.file("file",  new File(publishingFolder))(Resolver.mavenStylePatterns)),
      publishMavenStyle := true
    )
}