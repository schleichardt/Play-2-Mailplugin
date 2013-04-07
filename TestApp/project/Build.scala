import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "TestApp"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "info.schleichardt" %% "play-2-mailplugin" % "1.0-SNAPSHOT"
      , "com.icegreen" % "greenmail" % "1.3" % "test"
      , "junit" % "junit-dep" % "4.11" % "test"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
