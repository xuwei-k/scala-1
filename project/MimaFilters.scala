package scala.build

import sbt._, Keys._

object MimaFilters extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    val mimaReferenceVersion = settingKey[Option[String]]("Scala version number to run MiMa against")
  }
  import autoImport._

  override val globalSettings = Seq(
  )

  val mimaSettings: Seq[Setting[_]] = Def.settings(
//  mimaReportSignatureProblems := true, // TODO: enable
  )
}
