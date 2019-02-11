lazy val root = (project in file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.12.8",
    libraryDependencies += "org.apache.thrift" % "libthrift" % "0.12.0"
  )
