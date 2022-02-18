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

package scala.tools.nsc
package interactive
package tests

import core._
import scala.collection.mutable.ListBuffer

/** A base class for writing interactive compiler tests.
 *
 *  This class tries to cover common functionality needed when testing the presentation
 *  compiler: instantiation source files, reloading, creating positions, instantiating
 *  the presentation compiler, random stress testing.
 *
 *  By default, this class loads all scala and java classes found under `src/`, going
 *  recursively into subfolders. Loaded classes are found in `sourceFiles`. trait `TestResources`
 *  The presentation compiler is available through `compiler`.
 *
 *  It is easy to test member completion, type and hyperlinking at a given position. Source
 *  files are searched for `TextMarkers`. By default, the completion marker is `/*!*/`, the
 *  typedAt marker is `/*?*/` and the hyperlinking marker is `/*#*/`. Place these markers in
 *  your source files, and the test framework will automatically pick them up and test the
 *  corresponding actions. Sources are reloaded by `askReload(sourceFiles)` (blocking
 *  call). All ask operations are placed on the work queue without waiting for each one to
 *  complete before asking the next. After all asks, it waits for each response in turn and
 *  prints the result. The default timeout is 1 second per operation.
 *
 *  To define a custom operation you have to:
 *
 *  	(1) Define a new marker by extending `TestMarker`
 *  	(2) Provide an implementation for the operation you want to check by extending `PresentationCompilerTestDef`
 *  	(3) Add the class defined in (1) to the set of executed test actions by calling `++` on `InteractiveTest`.
 *
 *  Then you can simply use the new defined `marker` in your test sources and the testing
 *  framework will automatically pick it up.
 *
 *  @see   Check existing tests under test/files/presentation
 */

