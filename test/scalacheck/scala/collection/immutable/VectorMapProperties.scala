package scala.collection.immutable

import org.scalacheck._
import Arbitrary.arbitrary
import Prop._
import Gen._

object VectorMapProperties extends Properties("immutable.VectorMap") {

  type K = Byte
  type V = Byte
  type T = (K, V)

  implicit def vectorMapArb[A: Arbitrary, B: Arbitrary]: Arbitrary[VectorMap[A, B]] =
    Arbitrary(implicitly[Arbitrary[Map[A, B]]].arbitrary.map{ m1 =>
      val m2 = VectorMap.from(m1)
      val xs = List.fill(scala.util.Random.nextInt(3))(implicitly[Arbitrary[A]].arbitrary.sample.get)
      // println(s"$m2 $xs")
      xs.foldLeft(m2)(_ - _)
    })

/*
  // https://github.com/scala/bug/issues/11218
  property("init last") = forAll { (x1: VectorMap[Byte, Byte]) =>
    x1.nonEmpty ==> {
      println((x1, x1.init, x1.last))
      x1 == (x1.init + x1.last)
    }
  }
*/

  property("iterator") = forAll { (x1: VectorMap[Byte, Byte]) =>
    if(x1.nonEmpty) {
      assert(x1.iterator.toList.last == x1.last)
      assert(x1.iterator.toList.head == x1.head)
      true
    } else {
      x1.iterator.isEmpty
    }
  }

  property("head tail") = forAll { (x1: VectorMap[Byte, Byte]) =>
    x1.nonEmpty ==> {
      x1 == (x1.tail + x1.head)
    }
  }


/*
  // https://github.com/scala/bug/issues/11220
  property("add remove") = forAll { (x1: VectorMap[Byte, Unit], values: List[Byte]) =>
    val values2 = values.filterNot(x1.keySet)
    val x2 = values2.foldLeft(x1)((a, b) => a + ((b, ())))
    val x3 = values2.foldLeft(x2)(_ remove _)
    x1 == x3
  }
*/

  property("internal underlying index match") = forAll { vm: VectorMap[K, V] =>
    !vm.isEmpty ==> {
      val last = vm.keys.last
      vm.fields(vm.underlying(last)._1) == last
    }
  }

  property("internal underlying and field length") = forAll { vm: VectorMap[K, V] =>
    vm.underlying.size == vm.keys.length
  }

  property("internal underlying and index are consistent after removal") = forAll { (vm: VectorMap[K, V]) =>
    vm.size >= 3 ==> {
      val v = Vector.from(vm)
      val random = v(new scala.util.Random().nextInt(vm.size))
      val removed = vm - random._1
      removed.underlying.forall { case (k, (s, v)) => removed.fields(s) == k }
      removed.fields.zipWithIndex.forall {
        case (k: K, s) => removed.underlying(k)._1 == s
        case _ => true
      }
    }
  }
}
