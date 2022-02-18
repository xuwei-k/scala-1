Global / unusedCodeConfig ~= { c =>
  c.copy(
    excludePath = c.excludePath ++ Set(
      "compiler/scala/tools/nsc/ast/TreeBrowsers",
      "compiler/scala/tools/nsc/backend/jvm/analysis/NullnessAnalyzer",
    ).map("glob:src/" + _ + ".scala") ++ Set(
      "intellij",
      "interactive",
      "library",
      "library-aux",
      "manual",
      "partest",
      "partest-javaagent",
      "reflect",
      "repl",
      "repl-frontend",
      "scaladoc",
      "scalap",
      "tastytest",
      "testkit",
    ).map("glob:src/" + _ + "/**"),
  )
}
