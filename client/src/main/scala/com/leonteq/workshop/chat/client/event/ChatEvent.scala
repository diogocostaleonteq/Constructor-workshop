package com.leonteq.workshop.chat.client.event

import com.leonteq.service.messages.Service.ServiceHealth
import scala.swing.event.Event

sealed trait ChatEvent extends Event

object ChatEvent {
  case class LoggedIn(nickname: String)                                               extends ChatEvent
  case class LoginError(reason: Option[String])                                       extends ChatEvent
  case class LoggedOut(reason: Option[String])                                        extends ChatEvent
  case class TransmissionError(reason: String)                                        extends ChatEvent
  case class IncomingChatMessageEvent(timestamp: Long, from: String, message: String) extends ChatEvent
  case class OnlineUsersEvent(nicknames: Seq[String])                                 extends ChatEvent
  case class ServiceHealthUpdate(health: ServiceHealth)                               extends ChatEvent
}
