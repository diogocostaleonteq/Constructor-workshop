package com.leonteq.workshop.chat.client

import com.leonteq.constructor.workshop.api.chat_api.ChatMessageRequest
import com.leonteq.constructor.workshop.api.chat_api.ChatMessageResponse
import com.leonteq.constructor.workshop.api.chat_api.LoginRequest
import com.leonteq.constructor.workshop.api.chat_api.LoginResponse
import com.leonteq.constructor.workshop.api.chat_api.LogoutRequest
import com.leonteq.constructor.workshop.api.chat_api.LogoutResponse
import com.leonteq.service.modules.LeonteqClientToolkit
import com.leonteq.service.LeonteqServiceClient
import javax.inject.Singleton
import scalapb.GeneratedMessage

@Singleton
case class ChatClientToolkit() extends LeonteqClientToolkit {

  override def serviceName: String = "constructor-workshop"

  lazy val loginClient: LeonteqServiceClient[
    GeneratedMessage,
    LoginRequest,
    LoginResponse
  ] = createClient(LoginRequest, LoginResponse)

  lazy val logoutClient: LeonteqServiceClient[
    GeneratedMessage,
    LogoutRequest,
    LogoutResponse
  ] = createClient(LogoutRequest, LogoutResponse)

  lazy val chatMessageClient: LeonteqServiceClient[
    GeneratedMessage,
    ChatMessageRequest,
    ChatMessageResponse
  ] = createClient(ChatMessageRequest, ChatMessageResponse)

}
