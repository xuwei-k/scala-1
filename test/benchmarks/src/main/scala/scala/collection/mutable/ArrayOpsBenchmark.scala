package scala.collection.mutable

import org.openjdk.jmh.annotations.Benchmark
import scala.util.Random

object ArrayOpsBenchmark {
  val array: Array[Int] = Array.fill(100000)(Random.nextInt)
  val sum: Int = array.sum
}

class OldIntArrayOps(override val repr: Array[Int]) extends AnyVal with ArrayLike[Int, Array[Int]] {
  override def length: Int = repr.length
  override def apply(index: Int): Int = repr(index)
  override def update(index: Int, elem: Int): Unit = { repr(index) = elem }
  override def seq = new WrappedArray.ofInt(repr)
  override protected[this] def newBuilder = new ArrayBuilder.ofInt
}

class ArrayOpsBenchmark {
  @Benchmark def foreachNew: Unit = {
    var i = 0
    ArrayOpsBenchmark.array.foreach(i += _)
    assert(i == ArrayOpsBenchmark.sum)
  }

  @Benchmark def foreachOld: Unit = {
    var i = 0
    new OldIntArrayOps(ArrayOpsBenchmark.array).foreach(i += _)
    assert(i == ArrayOpsBenchmark.sum)
  }
}
