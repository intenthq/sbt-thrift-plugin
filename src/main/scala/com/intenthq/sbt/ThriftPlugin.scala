package com.intenthq.sbt

import sbt._
import Keys._

import java.io.File
import scala.sys.process._

object ThriftPlugin extends AutoPlugin {

  def compileThrift(sourceDir: File, outputDir: File, thriftBin: String,
                    language: String, options: Seq[String], logger: Logger, cache: File): Seq[File] = {
    val doIt = FileFunction.cached(cache, inStyle = FilesInfo.lastModified, outStyle = FilesInfo.exists) { files =>
      if (!outputDir.exists)
        outputDir.mkdirs
      files.foreach { schema =>
        val cmd = s"""$thriftBin -gen ${language + options.mkString(":", ",", "")} -out $outputDir $schema"""
        logger.info(s"Compiling thrift schema with command: $cmd")
        val code = Process(cmd) ! logger
        if (code != 0) {
          sys.error(s"Thrift compiler exited with code $code")
        }
      }
      (outputDir ** s"*.$language").get.toSet
    }
    doIt((sourceDir ** "*.thrift").get.toSet).toSeq
  }

  object autoImport {
    val thrift = SettingKey[String]("thrift", "thrift executable")
    val thriftSourceDir = SettingKey[File]("source-directory", "Source directory for thrift files. Defaults to src/main/thrift")

    val thriftJavaEnabled = SettingKey[Boolean]("java-enabled", "java generation is enabled. Default - yes")
    val thriftGenerateJava = TaskKey[Seq[File]]("generate-java", "Generate java sources from thrift files")
    val thriftJavaOptions = SettingKey[Seq[String]]("thrift-java-options", "additional options for java thrift generation")
    val thriftOutputDir = SettingKey[File]("java-output-directory", "Directory where the java files should be placed. Defaults to sourceManaged")

    val thriftJsEnabled = SettingKey[Boolean]("js-enabled", "javascript generation is enabled. Default - no")
    val thriftGenerateJs = TaskKey[Seq[File]]("generate-js", "Generate javascript sources from thrift files")
    val thriftJsOptions = SettingKey[Seq[String]]("thrift-js-options", "additional options for js thrift generation")
    val thriftJsOutputDir = SettingKey[File]("js-output-directory", "Directory where generated javascript files should be placed. default target/thrift-js")

    val thriftRubyEnabled = SettingKey[Boolean]("ruby-enabled", "ruby generation is enabled. Default - no")
    val thriftGenerateRuby = TaskKey[Seq[File]]("generate-ruby", "Generate ruby sources from thrift files.")
    val thriftRubyOptions = SettingKey[Seq[String]]("thrift-ruby-options", "additional options for ruby thrift generation")
    val thriftRubyOutputDir = SettingKey[File]("ruby-output-directory", "Directory where generated ruby files should be placed. default target/thrift-ruby")

    val thriftPythonEnabled = SettingKey[Boolean]("python-enabled", "python generation is enabled. Default - no")
    val thriftGeneratePython = TaskKey[Seq[File]]("generate-python", "Generate python sources from thrift files.")
    val thriftPythonOptions = SettingKey[Seq[String]]("thrift-python-options", "additional options for python thrift generation")
    val thriftPythonOutputDir = SettingKey[File]("python-output-directory", "Directory where generated python files should be placed. default target/thrift-python")

    val thriftDelphiEnabled = SettingKey[Boolean]("delphi-enabled", "delphi generation is enabled. Default - no")
    val thriftGenerateDelphi = TaskKey[Seq[File]]("generate-delphi", "Generate delphi sources from thrift files.")
    val thriftDelphiOptions = SettingKey[Seq[String]]("thrift-delphi-options", "additional options for delphi thrift generation")
    val thriftDelphiOutputDir = SettingKey[File]("delphi-output-directory", "Directory where generated delphi files should be placed. default target/thrift-delphi")
  }

  import autoImport._

  override def requires = sbt.plugins.JvmPlugin
  override def trigger = allRequirements

  val Thrift = config("thrift")

  val thriftSettings: Seq[Setting[_]] = inConfig(Thrift)(Seq(
    thrift := "thrift",
    thriftSourceDir := { sourceDirectory.value / "main" / "thrift" },
    thriftJavaEnabled := true,
    thriftJavaOptions := Seq(),
    thriftOutputDir := { sourceManaged.value / "main" },
    thriftGenerateJava := (Def.taskDyn {
      if (thriftJavaEnabled.value)
        Def.task {
          compileThrift(thriftSourceDir.value, thriftOutputDir.value, thrift.value, "java", thriftJavaOptions.value, streams.value.log, streams.value.cacheDirectory / "thrift-java")
        }
      else
        Def.task {
          Seq.empty[File]
        }
    }).value,
    thriftJsEnabled := false,
    thriftJsOptions := Seq(),
    thriftJsOutputDir := file("target/gen-js"),
    thriftGenerateJs := (Def.taskDyn {
      if (thriftJsEnabled.value)
        Def.task {
          compileThrift(thriftSourceDir.value, thriftJsOutputDir.value, thrift.value, "js", thriftJsOptions.value, streams.value.log, streams.value.cacheDirectory / "thrift-js")
          Seq.empty[File]
        }
      else
        Def.task {
          Seq.empty[File]
        }
    }).value,
    thriftRubyEnabled := false,
    thriftRubyOptions := Seq(),
    thriftRubyOutputDir := file("target/gen-ruby"),
    thriftGenerateRuby := (Def.taskDyn {
      if (thriftRubyEnabled.value)
        Def.task {
          compileThrift(thriftSourceDir.value, thriftRubyOutputDir.value, thrift.value, "rb", thriftRubyOptions.value, streams.value.log, streams.value.cacheDirectory / "thrift-rb")
          Seq.empty[File]
        }
      else
        Def.task {
          Seq.empty[File]
        }
    }).value,
    thriftPythonEnabled := false,
    thriftPythonOptions := Seq(),
    thriftPythonOutputDir := file("target/gen-python"),
    thriftGeneratePython := (Def.taskDyn {
      if (thriftPythonEnabled.value)
        Def.task {
          compileThrift(thriftSourceDir.value, thriftPythonOutputDir.value, thrift.value, "py", thriftPythonOptions.value, streams.value.log, streams.value.cacheDirectory / "thrift-py")
          Seq.empty[File]
        }
      else
        Def.task {
          Seq.empty[File]
        }
    }).value,
    thriftDelphiEnabled := false,
    thriftDelphiOptions := Seq(),
    thriftDelphiOutputDir := file("target/gen-delphi"),
    thriftGenerateDelphi := (Def.taskDyn {
      if (thriftDelphiEnabled.value)
        Def.task {
          compileThrift(thriftSourceDir.value, thriftDelphiOutputDir.value, thrift.value, "delphi", thriftDelphiOptions.value, streams.value.log, streams.value.cacheDirectory / "thrift-delphi")
          Seq.empty[File]
        }
      else
        Def.task {
          Seq.empty[File]
        }
    }).value,
    managedClasspath := Classpaths.managedJars(Thrift, classpathTypes.value, update.value)
  )) ++ Seq[Setting[_]](
    watchSources ++= { (thriftSourceDir.value ** "*").get },
    sourceGenerators in Compile += (thriftGenerateJava in Thrift).taskValue,
    sourceGenerators in Compile += (thriftGenerateJs in Thrift).taskValue,
    sourceGenerators in Compile += (thriftGenerateRuby in Thrift).taskValue,
    sourceGenerators in Compile += (thriftGeneratePython in Thrift).taskValue,
    sourceGenerators in Compile += (thriftGenerateDelphi in Thrift).taskValue,
    ivyConfigurations += Thrift
  )

  override lazy val projectSettings = thriftSettings
}
