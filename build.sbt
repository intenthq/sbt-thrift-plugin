lazy val root = (project in file("."))
  .enablePlugins(SbtPlugin)
  .settings(
    sbtPlugin := true,
    organization := "com.intenthq.sbt",
    name := "sbt-thrift-plugin",
    version := "1.1.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    scalacOptions ++= Seq("-deprecation", "-feature"),

    // Settings for running SBT scripted tests for this plugin.
    // See https://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html for more
    // information.
    scriptedLaunchOpts := { scriptedLaunchOpts.value ++
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
    },

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    pomExtra := (
      <url>https://github.com/intenthq/sbt-thrift-plugin</url>
      <licenses>
        <license>
          <name>The MIT License (MIT)</name>
          <url>https://github.com/intenthq/sbt-thrift-plugin/blob/master/LICENSE</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:intenthq/sbt-thrift-plugin.git</url>
        <connection>scm:git:git@github.com:intenthq/sbt-thrift-plugin.git</connection>
      </scm>
      <developers>
        <developer>
          <id>intenthq</id>
          <name>Intent HQ</name>
        </developer>
      </developers>
    )
  )
