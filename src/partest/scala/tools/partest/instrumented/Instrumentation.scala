/*
 * Scala (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

package scala.tools.partest.instrumented

import scala.jdk.CollectionConverters._

case class MethodCallTrace(className: String, methodName: String, methodDescriptor: String) {
  override def toString(): String = className + "." + methodName + methodDescriptor
}
object MethodCallTrace {
  implicit val ordering: Ordering[MethodCallTrace] = Ordering.by(x => (x.className, x.methodName, x.methodDescriptor))
}

/**
 * An object that controls profiling of instrumented byte-code. The instrumentation is achieved
 * by using `java.lang.instrument` package. The instrumentation agent can be found in
 * `scala.tools.partest.javaagent` package.
 *
 * At the moment the following classes are being instrumented:
 *   * all classes with empty package
 *   * all classes from scala package (except for classes responsible for instrumentation)
 *
 * The canonical way of using instrumentation is have a test-case in `files/instrumented` directory.
 * The following code in main:
 *
 * {{{
 * import scala.tools.partest.instrumented.Instrumentation._
 * def main(args: Array[String]): Unit = {
 *   startProfiling()
 *   // should box the boolean
    println(true)
    stopProfiling()
    printStatistics()
 * }
 * }}}
 *
 *
 * should print:
 *
 * {{{
 * true
 * Method call statistics:
 * scala/Predef$.println(Ljava/lang/Object;)V: 1
 * scala/runtime/BoxesRunTime.boxToBoolean(Z)Ljava/lang/Boolean;: 1
 * }}}
 */

