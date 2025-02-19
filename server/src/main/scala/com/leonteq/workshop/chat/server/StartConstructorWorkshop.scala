package com.leonteq.workshop.chat.server

import cats.effect.IO
import cats.effect.Resource
import com.leonteq.constructor.utils.serviceStarter.ServiceStarter
import com.leonteq.constructor.utils.toolkit.api.HandlerStarter
import com.leonteq.constructor.utils.toolkit.scalapb._
import com.leonteq.constructor.workshop.server.BuildInfo
import com.leonteq.service.modules.Healthy
import com.leonteq.service.modules.LeonteqClientToolkit
import com.leonteq.service.modules.LeonteqServerToolkit
import com.leonteq.service.modules.Unhealthy
import com.leonteq.workshop.chat.server.handlers.ChatRequestHandler
import com.leonteq.workshop.chat.server.handlers.LoginRequestHandler
import com.leonteq.workshop.chat.server.handlers.LogoutRequestHandler
import com.leonteq.workshop.chat.server.service.UserService
import com.leonteq.workshop.config.ConstructorWorkshopConfig
import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters._

class StartConstructorWorkshopCore
    extends ServiceStarter[ConstructorWorkshopConfig, Any, LeonteqServerToolkit, LeonteqClientToolkit](
      BuildInfo.serviceName,
      BuildInfo.version
    ) {

  private val toolkitResource: Resource[IO, ChatServerToolkit] = Resource.eval(IO(new ChatServerToolkit))

  override def useBlockingAsCompute: Boolean = true

  override def handlers: List[HandlerStarter[LeonteqServerToolkit]] = List(
    ScalaPbHandler(classOf[LoginRequestHandler]),
    ScalaPbHandler(classOf[LogoutRequestHandler]),
    ScalaPbHandler(classOf[ChatRequestHandler])
  )

  override def clients: List[LeonteqClientToolkit] = Nil

  private def healthCheckResource(onlineUsers: List[String]): Resource[IO, Unit] = toolkitResource.flatMap { toolkit =>
    Resource.eval(IO.delay(toolkit.registerHealthCheck("constructor-workshop-server", 30.seconds) { () =>
      if (onlineUsers.size > 10) Unhealthy("Too many online users") else Healthy
    }))
  }

  protected override def runService: ServiceStarterContext => IO[Unit] =
    _ =>
      (for {
        _   <- toolkitResource
        log <- createLogger[IO].toResource
        _   <- healthCheckResource(UserService.onlineUsers.asScala.toList)
        _   <- log.info("Health check registered").toResource
        _   <- log.info("Constructor Workshop is up and running").toResource
      } yield ()).use(_ => IO.never)

}

object StartConstructorWorkshop extends StartConstructorWorkshopCore
