package scala.collection.immutable

import org.junit.Assert.assertEquals
import org.junit.Test

class VectorMapTest {
  @Test
  def test: Unit = {
    import scala.collection.immutable.VectorMap
    val x1 = VectorMap.empty[Int, String]
    val x2 = (1 to 3).foldLeft(x1){
      case (a, b) => a + (b -> b.toString) 
    }
    val x3 = (1 to 2).foldLeft(x2){
      case (a, b) => a - b
    }
    assertEquals(1, x3.size)
    x3.toString
  }
}
