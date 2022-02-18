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

package scala.tools.testkit

import scala.reflect.{ ClassTag, classTag, ensureAccessible }
import scala.util.chaining._
import java.lang.reflect.{ Array => _, _ }

/** This module contains reflection-related utilities.
 *
 *  This object contains methods that will not work on Scala.js nor Scala
 *  Native, making any test using `ReflectUtil` JVM-only.
 */

