object Test {
  def main(args: Array[String]): Unit = {
    println(Macro.inc()) // compile error. "macro applications do not support named and/or default arguments"
    println(Macro.inc(3)) // success
  }
}
