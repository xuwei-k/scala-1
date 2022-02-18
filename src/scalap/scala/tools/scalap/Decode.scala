/*
 * Scala classfile decoder (https://www.scala-lang.org)
 *
 * Copyright EPFL and Lightbend, Inc.
 *
 * Licensed under Apache License 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0).
 *
 * See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 */

// $Id$

package scala.tools.scalap

import scala.tools.scalap.scalax.rules.scalasig._
import scala.reflect.internal.util.ScalaClassLoader
import scala.reflect.internal.util.ScalaClassLoader.appLoader
import scala.reflect.internal.pickling.ByteCodecs

import ClassFileParser.{ ConstValueIndex, Annotation }
import Main.{ SCALA_SIG_ANNOTATION, BYTES_VALUE }

/** Temporary decoder.  This would be better off in the scala.tools.nsc
 *  but right now the compiler won't acknowledge scala.tools.scalap
 *  when it's bootstrapping, so the reference has to go from here to there.
 */


