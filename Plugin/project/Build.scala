import sbt._
import Keys._
import PlayProject._

object MailerBuild extends Build {

  lazy val moduleVersion =  "0.3-SNAPSHOT"
  lazy val moduleOrganization = "info.schleichardt"
  lazy val moduleName = "Play-2-Mailplugin"
  //lazy val moduleScalaVersion = "2.9.1"
  //lazy val playVersion =  "2.0.2"
  //lazy val typesafeRepo = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  lazy val publishingFolder = Path.userHome.absolutePath+"/Projekte/schleichardt.github.com/jvmrepo"

  val moduleDependencies = Seq(
       "org.apache.commons" % "commons-email" % "1.2"
  )

  lazy val main = PlayProject(moduleName, moduleVersion, moduleDependencies).settings(
      organization := moduleOrganization,
      publishTo := Some(Resolver.file("file",  new File(publishingFolder))(Resolver.mavenStylePatterns)),
      publishMavenStyle := true
      //resolvers += typesafeRepo,
      //libraryDependencies += "play" %% "play" % playVersion,
    )
}