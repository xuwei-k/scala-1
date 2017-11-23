// workaround https://github.com/sbt/sbt/issues/3427

import java.io.File
import java.util.Optional
import xsbti.{AnalysisCallback, Reporter}
import xsbti.compile._

compilers in ThisBuild := {
  val options = classpathOptions.value
  sbt.internal.inc.ZincUtil.compilers(
    instance = Keys.scalaInstance.value,
    classpathOptions = options,
    javaHome = None,
    new ScalaCompiler {
      override def classpathOptions = options

      // https://github.com/scala/scala/blob/v2.13.0-M2/src/compiler/scala/tools/nsc/Main.scala
      // https://github.com/scala/scala/blob/v2.13.0-M2/src/compiler/scala/tools/nsc/Driver.scala#L40
      private[this] val mainMethod = {
        val clazz = scalaInstance.loader.loadClass("scala.tools.nsc.Main")
        clazz.getMethod("process", classOf[Array[String]])
      }

      private[this] def callScalacMain(args: Array[String]): Boolean = {
        mainMethod.invoke(null, args).asInstanceOf[Boolean]
      }

      override def compile(
        source: Array[File],
        changes: DependencyChanges,
        options: Array[String],
        output: Output,
        callback: AnalysisCallback,
        reporter: Reporter,
        cache: GlobalsCache,
        log: xsbti.Logger,
        progressOpt: Optional[CompileProgress]
      ): Unit = {
        val dir = output.getSingleOutput.get
        IO.delete(dir)
        dir.mkdir
        val args: Array[String] = options ++ Array("-d", dir.getAbsolutePath) ++ source.map(_.getAbsolutePath)
        println("=============== args =================")
        args.foreach(println)
        println("======================================")
        callScalacMain(args)
      }

      override def compile(
        source: Array[File],
        changes: DependencyChanges,
        callback: AnalysisCallback,
        log: xsbti.Logger,
        reporter: Reporter,
        progress: CompileProgress,
        compiler: CachedCompiler
      ): Unit = {
        ???
      }

      override def scalaInstance =
        Keys.scalaInstance.value
    }
  )
}
