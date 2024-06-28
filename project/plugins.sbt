// Comment to get more information during initialization
//logLevel := Level.Warn


addSbtPlugin("org.playframework" % "sbt-plugin" % "3.0.3")

addSbtPlugin("com.jamesward" %% "play-auto-refresh" % "0.0.20")

ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)