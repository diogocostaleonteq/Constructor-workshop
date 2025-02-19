package com.leonteq.workshop.chat.client

import com.leonteq.constructor.workshop.api.chat_api._
import com.leonteq.workshop.chat.client.event.ChatEvent.LoggedIn
import com.leonteq.workshop.chat.client.event.ChatEvent.LoggedOut
import com.leonteq.workshop.chat.client.event.ChatEvent.LoginError
import com.leonteq.workshop.chat.client.event.ChatEvent.TransmissionError
import com.typesafe.scalalogging.StrictLogging
import scala.swing.Publisher
import scala.util.Failure
import scala.util.Success

case class ChatServerClient(toolkit: ChatClientToolkit) extends Publisher with StrictLogging {

  def login(nickname: String): Unit = {
    val request = LoginRequest(Some(nickname))
    toolkit
      .loginClient
      .call(request)
      .onComplete {
        case Success(response)  =>
          if (response.loginState.contains(LoginState.OK))
            publish(LoggedIn(nickname))
          else
            publish(LoginError(Some(loginErrorMessage(response.loginState))))
        case Failure(exception) =>
          publish(LoginError(Some(exception.getMessage)))
      }(toolkit.executionContext)
  }

  def logout(nickname: String): Unit = {
    val request = LogoutRequest(Some(nickname))
    toolkit
      .logoutClient
      .call(request)
      .onComplete {
        case Success(_)         =>
          publish(LoggedOut(Some("Logged out successfully")))
        case Failure(exception) =>
          publish(TransmissionError(exception.getMessage))
      }(toolkit.executionContext)
  }

  def sendMessage(nickname: String, message: String): Unit =
    if (message.nonEmpty) {
      val request = ChatMessageRequest(Some(nickname), Some(message))
      toolkit
        .chatMessageClient
        .call(request)
        .onComplete {
          case Success(_)         =>
            logger.info(s"Message sent: $message")
          case Failure(exception) =>
            publish(TransmissionError(exception.getMessage))
        }(toolkit.executionContext)
    }

  private def loginErrorMessage(state: Option[LoginState]) = state match {
    case Some(LoginState.NICKNAME_NOT_AVAILABLE) => "Nickname already in use"
    case Some(LoginState.ERROR)                  => "Login error"
    case _                                       => "Unknown login error"
  }

}
