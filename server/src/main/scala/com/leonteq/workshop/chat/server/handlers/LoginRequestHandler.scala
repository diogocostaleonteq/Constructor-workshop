package com.leonteq.workshop.chat.server.handlers

import cats.effect.IO
import com.leonteq.constructor.utils.toolkit.scalapb.ScalaPbHandler
import com.leonteq.constructor.utils.validation.syntax.option._
import com.leonteq.constructor.utils.validation.syntax.result._
import com.leonteq.constructor.workshop.api.chat_api.LoginRequest
import com.leonteq.constructor.workshop.api.chat_api.LoginResponse
import com.leonteq.constructor.workshop.api.chat_api.LoginState
import com.leonteq.constructor.workshop.api.chat_api.OnlineUsersUpdate
import com.leonteq.workshop.chat.server.validators.UsernameValidator
import com.leonteq.workshop.chat.server.validators.UsernameValidator.UsernameTakenError
import com.leonteq.workshop.chat.server.ChatServerToolkit
import com.typesafe.scalalogging.StrictLogging
import javax.inject.Inject

class LoginRequestHandler @Inject() (toolkit: ChatServerToolkit)
    extends ScalaPbHandler[LoginRequest, LoginResponse]
    with StrictLogging {

  override def handle(request: LoginRequest): IO[LoginResponse] =
    (for {
      _              <- IO(logger.info(s"LoginRequestHandler handle login request $request"))
      username       <- request.nickname.orMissing[IO]("LogoutRequest.userName")
      onlineUsers     = toolkit.onlineUsers
      messageProducer = toolkit.messageProducer
      errors         <- handleUsernameValidation(username)
      state           = responseState(errors)
      _              <- IO(if (state == LoginState.OK) onlineUsers.addOne(username))
      _              <- IO(messageProducer.sendMessage(OnlineUsersUpdate(onlineUsers.toSeq)))
    } yield LoginResponse(Some(state))).handleErrorWith(_ => IO.pure(LoginResponse(Some(LoginState.ERROR))))

  private def handleUsernameValidation(username: String): IO[List[String]] = {
    val validator = new UsernameValidator(toolkit.onlineUsers.toList)
    for {
      validationResult <- validator.apply(username)
      errors            = validationResult.getObjectErrors("LoginRequest").map(_.defaultMessage)
    } yield errors
  }

  private def responseState(errors: List[String]): LoginState =
    if (errors.contains(UsernameTakenError)) LoginState.NICKNAME_NOT_AVAILABLE
    else LoginState.OK

}
