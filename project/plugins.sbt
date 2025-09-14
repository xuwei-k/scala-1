scalacOptions ++= Seq(
  "-unchecked",
  "-feature",
  "-deprecation",
  "-Xlint:-unused,_",
  "-Werror",
  "-Wconf:msg=IntegrationTest .* is deprecated:s,msg=itSettings .* is deprecated:s")

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.18.0"

libraryDependencies += "biz.aQute.bnd" % "biz.aQute.bndlib" % "6.1.0"

libraryDependencies ++= Seq(
  "org.eclipse.jgit" % "org.eclipse.jgit" % "4.11.9.201909030838-r",
  "org.slf4j" % "slf4j-nop" % "2.0.0",
  "com.googlecode.java-diff-utils" % "diffutils" % "1.3.0",
)

Global / concurrentRestrictions := Seq(
  Tags.limitAll(1) // workaround for https://github.com/sbt/sbt/issues/2970
)

addSbtPlugin("com.github.sbt" % "sbt-header" % "5.11.0")
