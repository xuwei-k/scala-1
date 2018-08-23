package scala
package collection
package immutable

import scala.collection.mutable.{Builder, ImmutableBuilder}
import scala.annotation.unchecked.{uncheckedVariance => uV}

/** This class implements immutable maps using a vector/map-based data structure, which preserves insertion order.
  *
  *  Unlike `ListMap`, `VectorMap` has amortized effectively constant lookup at the expense
  *  of using extra memory and generally lower performance for other operations
  *
  *  @tparam K      the type of the keys contained in this vector map.
  *  @tparam V      the type of the values associated with the keys in this vector map.
  *
  * @author Matthew de Detrich
  * @version 2.13
  * @since 2.13
  * @define coll immutable vector map
  * @define Coll `immutable.VectorMap`
  */
final class VectorMap[K, +V] private[immutable] (
    private[immutable] val fields: Vector[K],
    private[immutable] val underlying: Map[K, V])
    extends AbstractMap[K, V]
    with SeqMap[K, V]
    with MapOps[K, V, VectorMap, VectorMap[K, V]]
    with StrictOptimizedIterableOps[(K, V), Iterable, VectorMap[K, V]] {
  override protected[this] def className: String = "VectorMap"

  def updated[V1 >: V](key: K, value: V1): VectorMap[K, V1] = {
    underlying.get(key) match {
      case Some(oldIndexWithValue) =>
        new VectorMap(fields,
          underlying.updated(key, value))
      case None =>
        new VectorMap(
          fields :+ key,
          underlying.updated(key, value))
    }
  }

  override def withDefault[V1 >: V](d: K => V1): Map.WithDefault[K, V1] =
    new Map.WithDefault(this, d)

  override def withDefaultValue[V1 >: V](d: V1): Map.WithDefault[K, V1] = new Map.WithDefault[K, V1](this, _ => d)

  def iterator: Iterator[(K, V)] = new AbstractIterator[(K, V)] {
    private val fieldsIterator = fields.iterator

    override def hasNext: Boolean = fieldsIterator.hasNext

    override def next(): (K, V) = {
      val field = fieldsIterator.next()
      (field, underlying(field))
    }
  }

  def get(key: K): Option[V] = underlying.get(key)

  def remove(key: K): VectorMap[K, V] = {
    underlying.get(key) match {
      case Some(_) =>
        new VectorMap(fields.filterNot(_ == key), underlying - key)
      case _ =>
        this
    }
  }

  override def mapFactory: MapFactory[VectorMap] = VectorMap

  override def size: Int = fields.size

  override def knownSize: Int = fields.size

  override def isEmpty: Boolean = fields.isEmpty

  override final def contains(key: K): Boolean = underlying.contains(key)

  override def head: (K, V) = iterator.next()

  override def last: (K, V) = {
    val last = fields.last
    (last, underlying(last))
  }

  override def lastOption: Option[(K, V)] = {
    fields.lastOption match {
      case Some(last) => Some(last, underlying(last))
      case None => None
    }
  }

  override def tail: VectorMap[K, V] = {
    new VectorMap(fields.tail, underlying.remove(fields.head))
  }

  override def init: VectorMap[K, V] = {
    new VectorMap(fields.init, underlying.remove(fields.last))
  }

  override def foreach[U](f: ((K, V)) => U): Unit = iterator.foreach(f)

  override def keys: Iterable[K] = fields.toIterable

  override def values: Iterable[V] = underlying.values.toSeq
}

object VectorMap extends MapFactory[VectorMap] {

  def empty[K, V]: VectorMap[K, V] =
    new VectorMap[K, V](Vector.empty[K],
      if (VectorMap.useBaseline)
        OldHashMap.empty[K, V]
      else
        HashMap.empty[K, V]
    )

  def from[K, V](it: collection.IterableOnce[(K, V)]): VectorMap[K, V] =
    it match {
      case vm: VectorMap[K, V] => vm
      case _                   => (newBuilder[K, V] ++= it).result()
    }

  def newBuilder[K, V]: Builder[(K, V), VectorMap[K, V]] =
    new ImmutableBuilder[(K, V), VectorMap[K, V]](empty) {
      def addOne(elem: (K, V)): this.type = { elems = elems + elem; this }
    }

  // getenv not getProperty for Scala.js friendliness.
  // TODO remove before 2.13.0-RC1? see scala/collection-strawman#572
  private final val useBaseline: Boolean =
    System.getenv("SCALA_COLLECTION_IMMUTABLE_USE_BASELINE") == "true"
}
