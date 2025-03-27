name := "play-task"
organization := "com.example"

version := "1.0-SNAPSHOT"

scalaVersion := "2.12.10"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Dependencies
libraryDependencies ++= Seq(
  guice,
  "org.webjars" % "swagger-ui" % "3.43.0",
  "io.swagger" %% "swagger-play2" % "1.7.1"
)

// Uncomment if you need additional imports
// TwirlKeys.templateImports += "com.example.controllers._"
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
