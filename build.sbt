sbtPlugin := true

organization := "com.intenthq.sbt"

name := "sbt-thrift-plugin"

version := "1.0.0"

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-deprecation", "-feature")

publishMavenStyle := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
