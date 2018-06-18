enablePlugins(ScalaJSPlugin)

name := "sharetime"

version := "0.1"

scalaVersion := "2.12.6"

// This is an application with a main method
scalaJSUseMainModuleInitializer := true

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.5"

libraryDependencies += "org.querki" %%% "jquery-facade" % "1.2"

libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.2"

skip in packageJSDependencies := false
jsDependencies +=
  "org.webjars" % "jquery" % "2.2.1" / "jquery.js" minified "jquery.min.js"

jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
