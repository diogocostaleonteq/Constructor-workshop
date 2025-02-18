package com.leonteq.workshop.chat.client

import com.leonteq.constructor.workshop.api.chat_api.ChatMessageRequest
import com.leonteq.constructor.workshop.api.chat_api.OnlineUsersUpdate
import com.leonteq.service.modules.LeonteqClientToolkit
import com.leonteq.workshop.chat.client.event.ChatEvent.IncomingChatMessageEvent
import com.leonteq.workshop.chat.client.event.ChatEvent.OnlineUsersEvent
import com.leonteq.workshop.chat.client.event.ChatEvent.ServiceHealthUpdate
import scala.swing.Publisher

case class ChatServerListener(toolkit: LeonteqClientToolkit) extends Publisher {
  toolkit.listenToHealthForTag("constructor-workshop-server")(health => publish(ServiceHealthUpdate(health)))
  toolkit.listen(OnlineUsersUpdate)(update => publish(OnlineUsersEvent(update.nickname)))
  toolkit.listen(ChatMessageRequest)(response =>
    publish(IncomingChatMessageEvent(System.currentTimeMillis(), response.getNickname, response.getMessage))
  )
}
