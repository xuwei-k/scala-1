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

package scala.tools.partest

import scala.reflect.runtime.{universe => ru}
import scala.tools.nsc._

/** For testing compiler internals directly.
 *  Each source code string in "sources" will be compiled, and
 *  the check function will be called with the source code and the
 *  resulting CompilationUnit.  The check implementation should
 *  test for what it wants to test and fail (via assert or other
 *  exception) if it is not happy.
 */

