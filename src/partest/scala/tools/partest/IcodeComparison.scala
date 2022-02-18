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

import scala.tools.partest.nest.FileManager.compareContents

/** A class for testing icode.  All you need is this in a
 *  partest source file --
 *  {{{
 *    object Test extends IcodeComparison
 *  }}}
 *  -- and the generated output will be the icode for everything
 *  in that file.  See scaladoc for possible customizations.
 *  TODO promote me to partest
 */

