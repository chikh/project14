import com.tuplejump.sbt.yeoman.Yeoman

name := """qiwi-test-task"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).settings(
  Yeoman.yeomanSettings: _*
)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.google.inject" % "guice" % "3.0"
)

resolvers += Resolver.sonatypeRepo("snapshots")