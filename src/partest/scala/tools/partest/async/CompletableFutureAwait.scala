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

package scala.tools.partest.async

import java.util.Objects
import java.util.concurrent.{CompletableFuture, Executor}
import java.util.function.BiConsumer

import scala.annotation.compileTimeOnly
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.tools.testkit.async.AsyncStateMachine
import scala.util.{Failure, Success, Try}

object CompletableFutureAwait {
  def async[T](executor: Executor)(body: T): CompletableFuture[T] = macro impl
  @compileTimeOnly("[async] `await` must be enclosed in `async`")
  def await[T](completableFuture: CompletableFuture[T]): T = ???
  def impl(c: blackbox.Context)(executor: c.Tree)(body: c.Tree): c.Tree = {
    import c.universe._
    val awaitSym = typeOf[CompletableFutureAwait.type].decl(TermName("await"))
    def mark(t: DefDef): Tree = c.internal.markForAsyncTransform(c.internal.enclosingOwner, t, awaitSym, Map.empty)
    val name = TypeName("stateMachine$async")
    q"""
      final class $name extends _root_.scala.tools.partest.async.CompletableFutureStateMachine($executor) {
        ${mark(q"""override def apply(tr$$async: _root_.scala.util.Try[_root_.scala.AnyRef]) = ${body}""")}
      }
      new $name().start().asInstanceOf[${c.macroApplication.tpe}]
    """
  }
}


