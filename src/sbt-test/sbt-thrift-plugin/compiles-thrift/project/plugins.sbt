sys.props.get("plugin.version") match {
  case Some(x) => addSbtPlugin("com.intenthq.sbt" % "sbt-thrift-plugin" % x)
  case _ => addSbtPlugin("com.intenthq.sbt" % "sbt-thrift-plugin" % "1.1.0")
  // sys.error("""|The system property 'plugin.version' is not defined.
  //                        |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}
