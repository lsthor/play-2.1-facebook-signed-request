import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "play_facebook"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "commons-codec" % "commons-codec" % "1.7"
    )

    val main = play.Project(appName, appVersion, appDependencies).settings(
      // Add your own project settings here      
    )

}
