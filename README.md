# SBT Thrift Plugin [![Build Status](https://travis-ci.org/intenthq/sbt-thrift-plugin.svg?branch=master)](https://travis-ci.org/intenthq/sbt-thrift-plugin)

**A Thrift plugin for compiling Thrift sources into Java and adding the generated classes into the compile scope automagically.**

In addition to Java, this plugin can also compile to the following languages:

* Javascript
* Ruby
* Python
* Delphi

These aren't supported as sources by SBT but will be available in a `target/gen-<lang>` directory for use by yourself in any suitable way you can think of.

By default, the plugin will compile to Java only. See the settings section below for details on how to enable other languages.

## Usage

This is an SBT auto-plugin so all you have to do is add the following to your `project/plugins.sbt`:

```scala
addSbtPlugin("com.intenthq.sbt" % "sbt-thrift-plugin" % "1.1.0")
```

> *Note:* SBT `0.13.x` and earlier users should use `1.0.5` which is the last release compatible with these versions of SBT. Only SBT `1.x` is supported from `1.1.0` onwards.

### Installing Thrift

This plugin depends on having the `thrift` binary in your `PATH` — it does not add any speciifc dependency on `libthrift` for you. This means you should:

* Make sure the correct version of `thrift` you need is installed on your system and in your `PATH`.
* Make sure you add a dependency on the correct version of `libthrift` to the application.

We have automated tests for this plugin running using the latest Thrift version (currently `0.12.0`) and `0.9.x` (used by the Spark ecosystem).

## Settings

To set any settings, you'll need to import the settings into your `build.sbt`:

```scala
import com.intenthq.sbt.ThriftPlugin._
```

The following languages are supported by this plugin:

* Java
* Javascript (referred to in setting names as `Js`)
* Ruby
* Python
* Delphi

For all of these languages, the following SBT settings are available (where `<lang>` is the name of the language above):

* `thrift<lang>Enabled`: Whether to enable generation of code for this language. Defaults to `true` for Java, `false` for all other languages.
* `thrift<lang>Options`: Options to pass the Thrift compiler when generating the source files. See `thrift -help` for the optional available for your chosen language. Defaults to an empty set of options for all languages.
* `thrift<lang>OutputDir`: The directory to save the generated files into. Defaults to `target/gen-<lang>`.

All languages have a task, `thriftGenerate<lang>`, to generate the files manually. This task will automatically be run by SBT when running `compile`, as it is added as an SBT source generator.

## Acknowledgements

The code for this plugin was adapted from the following sources:

* [Original Thrift plugin for SBT by Andrew Headrick](https://github.com/bigtoast/sbt-thrift)
* [Contributions in pull request by Akon32](https://github.com/bigtoast/sbt-thrift/pull/9)
