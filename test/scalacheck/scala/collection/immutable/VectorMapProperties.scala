package scala.collection.immutable

import org.scalacheck._
import Arbitrary.arbitrary
import Prop._
import Gen._

object VectorMapProperties extends Properties("immutable.VectorMap") {

  property("internal underlying and field length") = forAll { (m: Map[Int, Int]) => {
      val vm = VectorMap.from(m)
      vm.underlying.size == vm.fields.length
    }
  }

}
