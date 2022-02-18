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

import java.io.OutputStreamWriter
import java.net.URI
import java.nio.charset.StandardCharsets
import java.util.Locale

import javax.tools._

import scala.jdk.CollectionConverters._
import scala.reflect.internal.util.AbstractFileClassLoader
import scala.reflect.io.{AbstractFile, VirtualDirectory}
import scala.tools.nsc.classpath.{AggregateClassPath, VirtualDirectoryClassPath}
import scala.tools.nsc.{Global, Settings}

/** Utilities for testing with javac/scalac without using the actual filesystem,
  * presumably because one doesn't wish to deal with platform idiosyncrasies.
  */


final class VirtualFileManager(dir: VirtualDirectory, del: StandardJavaFileManager)
    extends ForwardingJavaFileManager[StandardJavaFileManager](del) {
  import JavaFileManager.Location
  import JavaFileObject.Kind

  override def getJavaFileForOutput(
    loc: Location,
    clasz: String,
    kind: Kind,
    sibling: FileObject,
  ): JavaFileObject = {
    assert(loc == StandardLocation.CLASS_OUTPUT, loc)
    assert(kind == Kind.CLASS, kind)
    val (file, uri) = mkFile(clasz)
    new SimpleJavaFileObject(uri, Kind.CLASS) {
      override def openOutputStream() = file.output
    }
  }

  override def getJavaFileForInput(loc: Location, clasz: String, kind: Kind): JavaFileObject = {
    if (loc == StandardLocation.CLASS_PATH) {
      assert(kind == Kind.CLASS, kind)
      val (file, uri) = mkFile(clasz)
      new SimpleJavaFileObject(uri, Kind.CLASS) {
        override def openInputStream() = file.input
      }
    } else super.getJavaFileForInput(loc, clasz, kind)
  }

  private def mkFile(clasz: String): (AbstractFile, URI) = {
    val parts = clasz.split('.')
    val pkg = parts.init.foldLeft[AbstractFile](dir)(_ subdirectoryNamed _)
    val file = pkg.fileNamed(parts.last + ".class")
    val uri = new URI("vc:/" + parts.mkString("/") + ".class")
    (file, uri)
  }
}


final class InMemorySourcefile(uri: URI, contents: String)
    extends SimpleJavaFileObject(uri, JavaFileObject.Kind.SOURCE) {
  override def getCharContent(ignoreEncodingErrors: Boolean) = contents
}
