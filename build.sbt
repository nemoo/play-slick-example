import scala.collection.Seq
import scala.sys.process.Process


name := """play-slick-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "3.3.3"

routesGenerator := InjectedRoutesGenerator

libraryDependencies += "org.playframework" %% "play-slick" % "6.1.0"
libraryDependencies += "org.playframework" %% "play-slick-evolutions" % "6.1.0"
libraryDependencies += "org.playframework" %% "play-json" % "3.0.2"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % "test"
libraryDependencies += "com.dimafeng" %% "testcontainers-scala-scalatest" % "0.40.15" % "test"
libraryDependencies += "com.dimafeng" %% "testcontainers-scala-postgresql" % "0.40.15" % "test"

libraryDependencies += "org.postgresql" % "postgresql" % "42.7.0"
libraryDependencies += specs2 % Test
libraryDependencies += guice
libraryDependencies += ws
libraryDependencies += caffeine

libraryDependencies += "org.playframework.silhouette" %% "play-silhouette" % "10.0.1"
libraryDependencies += "org.playframework.silhouette" %% "play-silhouette-password-bcrypt" % "10.0.1"
libraryDependencies += "org.playframework.silhouette" %% "play-silhouette-persistence" % "10.0.1"
libraryDependencies += "org.playframework.silhouette" %% "play-silhouette-crypto-jca" % "10.0.1"
libraryDependencies += "net.codingwell" %% "scala-guice" % "6.0.0"

ThisBuild / libraryDependencySchemes ++= Seq(
    "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)

val generateSha1: Unit =  {
    val sha1 = try {
        Process("git rev-parse HEAD").!!.linesIterator.toList.head
    } catch {
        case e: Exception => println("Could not extract sha1 hash, you need to install command line git."); "???"
    }
    IO.write(file("conf/version.conf"), s"""sha1="$sha1"""")
}

//enable displaying of link to intellij in error page
javaOptions += "-Dplay.editor=http://localhost:63342/api/file/?file=%s&line=%s"
