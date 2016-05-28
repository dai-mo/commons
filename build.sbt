import Dependencies._
import Common._


lazy val commons = (project in file(".")).
  configs(IntegrationTest).
  settings(commonSettings: _*).
  settings(Defaults.itSettings: _*).
  settings(
    name := "org.dcs.commons",
    moduleName := name.value,
    libraryDependencies ++= commonsDependencies
  )
