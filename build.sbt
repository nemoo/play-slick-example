name := """play-slick-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.10"

routesGenerator := InjectedRoutesGenerator

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
resolvers += Resolver.jcenterRepo

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-slick" % "3.0.1",
    "com.typesafe.play" %% "play-slick-evolutions" % "3.0.1",
    "com.typesafe.play" %% "play-json" % "2.7.1",
    "com.h2database" % "h2" % "1.4.192",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % "test",
    "com.github.takezoe" %% "blocking-slick-32" % "0.0.8",
    specs2 % Test,
    guice,
    ehcache,
    "com.mohiva" %% "play-silhouette" % "6.1.1",
    "com.mohiva" %% "play-silhouette-password-bcrypt" % "6.1.1",
    "com.mohiva" %% "play-silhouette-persistence" % "6.1.1",
    "com.mohiva" %% "play-silhouette-crypto-jca" % "6.1.1",

    "net.codingwell" %% "scala-guice" % "4.1.0",
    "com.iheart" %% "ficus" % "1.4.1"
)


