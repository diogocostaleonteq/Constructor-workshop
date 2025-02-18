import com.leonteq.constructor.build.Artifacts
import com.leonteq.constructor.build.SbtModules
import sbt.*

object Dependencies extends Artifacts {


  // GUI stuff
  private val ScalaSwing = "org.scala-lang.modules" %% "scala-swing" % "3.0.0"

  val ApiDependencies: SbtModules =
    Seq(
      D9ServiceCore,
      D9ServiceCoreScalaPB,
      D9ServiceScalaPB
    )

  val ConfigDependencies: SbtModules =
    Seq(
      ConstructorUtilsConfigLoader,
      Ip4sCore,
      PureconfigCats,
      PureconfigIp4s
    )

  val ServerDependencies: SbtModules =
    Seq(
      CatsCore,
      CatsEffect,
      D9ServiceCore,
      D9ServiceCoreScalaPB,
      ConstructorUtilsScalaPB,
      ConstructorUtilsScala,
      ConstructorUtilsServiceStarter,
      ConstructorUtilsToolkitScalaPb,
      ConstructorUtilsValidation,
      LogbackClassic,
      LogstashLogbackEncoder,
      Log4Cats
    )

  val ClientDependencies: SbtModules =
    Seq(
      D9ServiceCore,
      D9ServiceCoreScalaPB,
      ScalaSwing,
      LogbackClassic,
      Log4Cats
    )
}
