import Common._
import Dependencies._
import com.typesafe.sbt.GitPlugin.autoImport._
import sbtbuildinfo.BuildInfoPlugin.autoImport._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease._

lazy val commonsProjectName = "org.dcs.commons"
lazy val commonsProjectID   = "commons"
lazy val commonsProjectDir   = "."

lazy val commons = OsgiProject(commonsProjectID, commonsProjectName, commonsProjectDir).
  settings(libraryDependencies ++= commonsDependencies).
  settings(Seq(unmanagedSourceDirectories in Compile += baseDirectory.value / "generated" / "src" / "main" / "java",
    unmanagedSourceDirectories in Test += baseDirectory.value / "generated" / "src" / "test" / "java"))

headerLicense := Some(HeaderLicense.Custom(
  """Copyright (c) 2017-2018 brewlabs SAS
    |
    |Licensed under the Apache License, Version 2.0 (the "License");
    |you may not use this file except in compliance with the License.
    |You may obtain a copy of the License at
    |
    |    http://www.apache.org/licenses/LICENSE-2.0
    |
    |Unless required by applicable law or agreed to in writing, software
    |distributed under the License is distributed on an "AS IS" BASIS,
    |WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    |See the License for the specific language governing permissions and
    |limitations under the License.
    |
    |""".stripMargin
))

// ------- Versioning , Release Section --------

// Build Info
buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion)
buildInfoPackage := commonsProjectName

// Git
showCurrentGitBranch

git.useGitDescribe := true

git.baseVersion := "0.0.0"

val VersionRegex = "v([0-9]+.[0-9]+.[0-9]+)-?(.*)?".r

git.gitTagToVersionNumber := {
  case VersionRegex(v,"SNAPSHOT") => Some(s"$v-SNAPSHOT")
  case VersionRegex(v,"") => Some(v)
  case VersionRegex(v,s) => Some(s"$v-$s-SNAPSHOT")
  case v => None
}

lazy val bumpVersion = settingKey[String]("Version to bump - should be one of \"None\", \"Major\", \"Patch\"")
bumpVersion := "None"

releaseVersion := {
  ver => bumpVersion.value.toLowerCase match {
    case "none" => Version(ver).
      map(_.withoutQualifier.string).
      getOrElse(versionFormatError)
    case "major" => Version(ver).
      map(_.withoutQualifier).
      map(_.bump(sbtrelease.Version.Bump.Major).string).
      getOrElse(versionFormatError)
    case "patch" => Version(ver).
      map(_.withoutQualifier).
      map(_.bump(sbtrelease.Version.Bump.Bugfix).string).
      getOrElse(versionFormatError)
    case _ => sys.error("Unknown bump version - should be one of \"None\", \"Major\", \"Patch\"")
  }
}

releaseVersionBump := sbtrelease.Version.Bump.Minor
