enablePlugins(ScalaJSBundlerPlugin)

name := "todo-scalaz-react"
version := "0.1"
scalaVersion := "2.12.6"

scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds"
)

resolvers += Resolver.sonatypeRepo("releases")
addCompilerPlugin("org.spire-math" % "kind-projector" % "0.9.7" cross CrossVersion.binary)

scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scalaz" %%% "scalaz-core" % "7.2.25"
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % "1.2.1"
libraryDependencies += "com.github.japgolly.scalajs-react" %%% "ext-scalaz72" % "1.2.1"

npmDependencies in Compile ++= Seq(
  "react" -> "16.4.1",
  "react-dom" -> "16.4.1")
npmDevDependencies in Compile ++= Seq(
  "webpack-merge" -> "4.1.3")


webpackConfigFile in fullOptJS := Some(baseDirectory.value / "webpack.config.js")
version in webpack := "4.15.1"
version in startWebpackDevServer := "3.1.4"
