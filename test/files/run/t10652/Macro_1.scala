import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object Macro {
  def incImpl(c: blackbox.Context)(n: c.Tree) = {
    import c.universe._
    q"$n + 1"
  }

  implicit def inc(implicit n: Int = 0): Int = macro incImpl
}
