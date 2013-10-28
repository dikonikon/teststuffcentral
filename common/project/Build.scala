import sbt._
import Keys._
import play.Project._

object BuildSettings {
  val buildOrganization = "com.teststuffcentral"
  val buildVersion = "1.0-SNAPSHOT"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version := buildVersion
    )
}

object ApplicationBuild extends Build {
  import BuildSettings._

  val appName         = "common"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "com.typesafe.akka" %% "akka-actor" % "2.0.1"
  )


  val main = play.Project(appName, appVersion, appDependencies, settings = buildSettings).settings(
    // Add your own project settings here      
  )

}
