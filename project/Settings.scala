import com.leonteq.constructor.build.ConstructorServicePlugin.autoImport.*
import com.leonteq.constructor.build.ConstructorSettingsPlugin.autoImport.*
import com.leonteq.constructor.build.SbtModules
import com.leonteq.simplebuildtool.SbtSettings
import sbt.none
import sbt.settingKey
import sbt.Keys.*
import sbt.SettingKey
import sbt.ThisBuild
import sbtbuildinfo.BuildInfoKeys.{buildInfoKeys, buildInfoPackage}

object Settings {

  lazy val service: SettingKey[String] = settingKey[String]("The name of this service")

  lazy val Api: SbtSettings              = settings(Dependencies.ApiDependencies)
  lazy val Config: SbtSettings           = settings(Dependencies.ConfigDependencies)
  lazy val Server: SbtSettings          = settings(Dependencies.ServerDependencies)
  lazy val Client: SbtSettings         = settings(Dependencies.ClientDependencies)

  private lazy val CommonSettings: SbtSettings =
    Seq(
      organization         := s"com.leonteq.${normalize(teamName.value)}.${normalize(service.value)}",
      targetJvmVersion     := none,
      scalacOptions += "-Wconf:cat=other-pure-statement:silent",
      service              := (ThisBuild / service).value,
      serviceName          := s"${teamName.value}-${service.value}",
      projectName          := s"${teamName.value}-${service.value}",
      buildInfoKeys += service,
      buildInfoKeys += serviceName,
      buildInfoKeys += projectName
    )

  private def normalize(packageName: String): String =
    packageName.replaceAll("-", "")

  private def settings(dependencies: SbtModules): SbtSettings =
    CommonSettings ++ Seq(libraryDependencies ++= dependencies)

}
