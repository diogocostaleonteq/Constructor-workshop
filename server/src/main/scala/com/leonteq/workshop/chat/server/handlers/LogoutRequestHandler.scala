package com.leonteq.workshop.chat.server.handlers

import cats.effect.IO
import com.leonteq.constructor.utils.toolkit.scalapb.ScalaPbHandler
import com.leonteq.constructor.utils.validation.syntax.option._
import com.leonteq.constructor.workshop.api.chat_api.LogoutRequest
import com.leonteq.constructor.workshop.api.chat_api.LogoutResponse
import com.leonteq.constructor.workshop.api.chat_api.OnlineUsersUpdate
import com.leonteq.workshop.chat.server.ChatServerToolkit
import javax.inject.Inject

class LogoutRequestHandler @Inject() (toolkit: ChatServerToolkit)
    extends ScalaPbHandler[LogoutRequest, LogoutResponse] {

  override def handle(request: LogoutRequest): IO[LogoutResponse] =
    for {
      username       <- request.nickname.orMissing[IO]("LogoutRequest.userName")
      onlineUsers     = toolkit.onlineUsers
      messageProducer = toolkit.messageProducer
      _              <- IO(onlineUsers.remove(onlineUsers.indexOf(username)))
      _              <- IO(messageProducer.sendMessage(OnlineUsersUpdate(onlineUsers.toSeq)))
    } yield LogoutResponse()
}
