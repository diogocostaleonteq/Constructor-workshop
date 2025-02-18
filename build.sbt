import sbt.Keys._
import sbt._
import Settings._

inThisBuild(
  Seq(
    service     := "workshop",
    projectName := s"${teamName.value}-${service.value}",
    scalaVersion := "2.13.16"
  )
)

val NoPublishSettings: Seq[Def.Setting[_]] = Seq(
  publishLocal := {},
  publish := {}
)

val `api` =
  project
    .settings(Settings.Api)
    .settings(
      libraryDependencies ++= Seq(
        "com.thesamet.scalapb" %% "scalapb-runtime" % "0.11.17" % "protobuf"
      ),
      Compile / PB.targets := Seq(
        scalapb.gen() -> (Compile / sourceManaged).value / "compiled_protobuf"
      ),
      Compile / PB.protoSources := Seq(
        (Compile / sourceDirectory).value / "protobuf"
      )
    )
    .enablePlugins(ConstructorLibraryPlugin, BuildInfoPlugin)

val `config` =
  project
    .settings(Settings.Config)
    .enablePlugins(ConstructorLibraryPlugin)

val `server` =
  project
    .dependsOn(`api`, `config`)
    .settings(Settings.Server)
    .enablePlugins(ConstructorServicePlugin, ConstructorLibraryPlugin)

val `client` =
  project
    .dependsOn(`api`, `server`)
    .settings(Settings.Client)
    .enablePlugins(ConstructorLibraryPlugin)

lazy val `constructor-workshop` = (project in file("."))
  .aggregate(
    `api`,
    `server`,
    `client`,
    `config`
  )
  .settings(
    name := "constructor-workshop",
    version := "0.1.0"
  )
  .settings(LeonteqStandardBuild.Releasing.settings ++ NoPublishSettings)
  .enablePlugins(ConstructorRootPlugin, ConstructorLibraryPlugin)