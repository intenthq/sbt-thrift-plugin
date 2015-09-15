sbtPlugin := true

organization := "com.intenthq.sbt"

name := "sbt-thrift-plugin"

version := "1.0.1"

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-deprecation", "-feature")

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

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
