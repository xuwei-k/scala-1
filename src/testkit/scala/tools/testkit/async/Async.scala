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

package scala.tools.testkit.async

import java.util.Objects

import scala.annotation.compileTimeOnly
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.util.{Failure, Success, Try}

object Async {
  def async[T](body: T)(implicit executionContext: ExecutionContext): Future[T] = macro impl
  @compileTimeOnly("[async] `await` must be enclosed in an `async` block")
  def await[T](completableFuture: Future[T]): T = ???

  def impl(c: blackbox.Context)(body: c.Tree)(executionContext: c.Tree): c.Tree = {
    import c.universe._
    val awaitSym = typeOf[Async.type].decl(TermName("await"))
    def mark(t: DefDef): Tree = {
      c.internal.markForAsyncTransform(c.internal.enclosingOwner, t, awaitSym, Map.empty)
    }
    val name = TypeName("stateMachine$async")
    q"""
      final class $name extends _root_.scala.tools.testkit.async.AsyncAsMacroStateMachine($executionContext) {
        ${mark(q"""override def apply(tr$$async: _root_.scala.util.Try[_root_.scala.AnyRef]) = ${body}""")}
      }
      new $name().start().asInstanceOf[${c.macroApplication.tpe}]
    """
  }
}


