package com.leonteq.workshop.config

import com.leonteq.constructor.utils.configloader._
import com.leonteq.constructor.utils.configloader.PureconfigConfigLoader
import com.leonteq.constructor.utils.toolkit.api.ConfigLoader
import com.leonteq.constructor.utils.toolkit.api.ServerToolkitConfig
import pureconfig.generic.semiauto.deriveReader
import pureconfig.ConfigReader

final case class ConstructorWorkshopConfig(
    override val serviceName: String,
    override val serviceVersion: Int)
    extends ServerToolkitConfig

object ConstructorWorkshopConfig {

  implicit val configReaderConstructorWorkshopConfig: ConfigReader[ConstructorWorkshopConfig] =
    deriveReader[ConstructorWorkshopConfig]

  implicit val configLoaderConstructorWorkshopConfig: ConfigLoader[ConstructorWorkshopConfig] =
    PureconfigConfigLoader.forNamespace[ConstructorWorkshopConfig]("constructor-workshop")

}
