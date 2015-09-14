package com.intenthq.sbt

import sbt._
import Keys._

import java.io.File

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
    val thriftGenerateJava = TaskKey[Seq[File]]("thrift-generate-java", "Generate java sources from thrift files")
    val thriftJavaOptions = SettingKey[Seq[String]]("thrift-java-options", "additional options for java thrift generation")
    val thriftOutputDir = SettingKey[File]("java-output-directory", "Directory where the java files should be placed. Defaults to sourceManaged")

    val thriftJsEnabled = SettingKey[Boolean]("js-enabled", "javascript generation is enabled. Default - no")
    val thriftGenerateJs = TaskKey[Seq[File]]("thrift-generate-js", "Generate javascript sources from thrift files")
    val thriftJsOptions = SettingKey[Seq[String]]("thrift-js-options", "additional options for js thrift generation")
    val thriftJsOutputDir = SettingKey[File]("js-output-directory", "Directory where generated javascript files should be placed. default target/thrift-js")

    val thriftRubyEnabled = SettingKey[Boolean]("ruby-enabled", "ruby generation is enabled. Default - no")
    val thriftGenerateRuby = TaskKey[Seq[File]]("thrift-generate-ruby", "Generate ruby sources from thrift files.")
    val thriftRubyOptions = SettingKey[Seq[String]]("thrift-ruby-options", "additional options for ruby thrift generation")
    val thriftRubyOutputDir = SettingKey[File]("ruby-output-directory", "Directory where generated ruby files should be placed. default target/thrift-ruby")

    val thriftPythonEnabled = SettingKey[Boolean]("python-enabled", "python generation is enabled. Default - no")
    val thriftGeneratePython = TaskKey[Seq[File]]("thrift-generate-python", "Generate python sources from thrift files.")
    val thriftPythonOptions = SettingKey[Seq[String]]("thrift-python-options", "additional options for python thrift generation")
    val thriftPythonOutputDir = SettingKey[File]("python-output-directory", "Directory where generated python files should be placed. default target/thrift-python")

    val thriftDelphiEnabled = SettingKey[Boolean]("delphi-enabled", "delphi generation is enabled. Default - no")
    val thriftGenerateDelphi = TaskKey[Seq[File]]("thrift-generate-delphi", "Generate delphi sources from thrift files.")
    val thriftDelphiOptions = SettingKey[Seq[String]]("thrift-delphi-options", "additional options for delphi thrift generation")
    val thriftDelphiOutputDir = SettingKey[File]("delphi-output-directory", "Directory where generated delphi files should be placed. default target/thrift-delphi")
  }

  import autoImport._

  override def requires = sbt.plugins.JvmPlugin

  override def trigger = allRequirements

  val thriftConfig = config("thriftPlugin")

  val thriftSettings: Seq[Setting[_]] = Seq(
    thrift := "thrift",
    thriftSourceDir <<= (sourceDirectory in Compile) { _ / "thrift" },
    thriftJavaEnabled := true,
    thriftJavaOptions := Seq("hashcode"),
    thriftOutputDir <<= (sourceManaged in Compile) { identity },
    thriftGenerateJava <<= (thriftJavaEnabled, thriftSourceDir, thriftOutputDir, thrift, thriftJavaOptions, streams).map { (te, tsd, tod, t, to, s) =>
      if (te) {
        compileThrift(tsd, tod, t, "java", to, s.log, s.cacheDirectory / "thrift-java") }
      else
        Seq()
    },
    sourceGenerators <+= thriftGenerateJava,
    thriftJsEnabled := false,
    thriftJsOptions := Seq(),
    thriftJsOutputDir := file("target/gen-js"),
    thriftGenerateJs <<= (thriftJsEnabled, thriftSourceDir, thriftJsOutputDir, thrift, thriftJsOptions, streams).map { (te, tsd, tod, t, to, s) =>
      if (te) {
        compileThrift(tsd, tod, t, "js", to, s.log, s.cacheDirectory / "thrift-js") }
      else
        Seq()
    },
    sourceGenerators <+= thriftGenerateJs,
    thriftRubyEnabled := false,
    thriftRubyOptions := Seq(),
    thriftRubyOutputDir := file("target/gen-ruby"),
    thriftGenerateRuby <<= (thriftRubyEnabled, thriftSourceDir, thriftRubyOutputDir, thrift, thriftRubyOptions, streams).map { (te, tsd, tod, t, to, s) =>
      if (te) {
        compileThrift(tsd, tod, t, "rb", to, s.log, s.cacheDirectory / "thrift-rb") }
      else
        Seq()
    },
    sourceGenerators <+= thriftGenerateRuby,
    thriftPythonEnabled := false,
    thriftPythonOptions := Seq(),
    thriftPythonOutputDir := file("target/gen-python"),
    thriftGeneratePython <<= (thriftPythonEnabled, thriftSourceDir, thriftPythonOutputDir, thrift, thriftPythonOptions, streams).map { (te, tsd, tod, t, to, s) =>
      if (te) {
        compileThrift(tsd, tod, t, "py", to, s.log, s.cacheDirectory / "thrift-py") }
      else
        Seq()
    },
    sourceGenerators <+= thriftGeneratePython,
    thriftDelphiEnabled := false,
    thriftDelphiOptions := Seq(),
    thriftDelphiOutputDir := file("target/gen-delphi"),
    thriftGenerateDelphi <<= (thriftDelphiEnabled, thriftSourceDir, thriftDelphiOutputDir, thrift, thriftDelphiOptions, streams).map { (te, tsd, tod, t, to, s) =>
      if (te) {
        compileThrift(tsd, tod, t, "delphi", to, s.log, s.cacheDirectory / "thrift-delphi") }
      else
        Seq()
    },
    sourceGenerators <+= thriftGenerateDelphi
  )

  override lazy val projectSettings =
    Seq(ivyConfigurations += thriftConfig) ++
    inConfig(Test)(thriftSettings) ++
    inConfig(Compile)(thriftSettings)

}
