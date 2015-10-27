# Thrift plugin for sbt 

# Instructions for use:
This is an SBT autoplugin so all you have to do is add the following to your `project/plugins.sbt`:
```
addSbtPlugin("com.intenthq.sbt" % "sbt-thrift-plugin" % "1.0.2")
```

## Settings

[See the autoImport object in the plugin for a list of the available settings](https://github.com/intenthq/sbt-thrift-plugin/blob/master/src/main/scala/com/intenthq/sbt/ThriftPlugin.scala)

## Acknowledgements

The code for this plugin was adapted from the following sources:

[Original Thrift plugin for SBT by Andrew Headrick](https://github.com/bigtoast/sbt-thrift)

[Contributions in pull request by Akon32](https://github.com/bigtoast/sbt-thrift/pull/9)
