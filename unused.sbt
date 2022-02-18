Global / unusedCodeConfig ~= { c =>
  c.copy(
    excludePath = c.excludePath ++ Set(
      "compiler/scala/tools/nsc/ast/TreeBrowsers",
      "compiler/scala/tools/nsc/backend/jvm/analysis/NullnessAnalyzer",
      "repl-frontend/scala/tools/nsc/interpreter/jline/Reader",
      "repl-frontend/scala/tools/nsc/interpreter/shell/Scripted",
      "partest/scala/tools/partest/sbt/Framework",
      "partest/scala/tools/partest/sbt/SBTRunner",
    ).map("glob:src/" + _ + ".scala") ++ Set(
      "glob:src/library/**",
      "glob:src/reflect/**",
    ),
  )
}
