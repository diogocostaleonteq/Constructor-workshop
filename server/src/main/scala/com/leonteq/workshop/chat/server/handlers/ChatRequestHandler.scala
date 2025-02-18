package com.leonteq.workshop.chat.server.handlers

import cats.effect.IO
import com.leonteq.constructor.utils.toolkit.scalapb.ScalaPbHandler
import com.leonteq.constructor.workshop.api.chat_api.ChatMessageRequest
import com.leonteq.constructor.workshop.api.chat_api.ChatMessageResponse
import com.leonteq.workshop.chat.server.ChatServerToolkit
import javax.inject.Inject

class ChatRequestHandler @Inject() (toolkit: ChatServerToolkit)
    extends ScalaPbHandler[ChatMessageRequest, ChatMessageResponse] {

  override def handle(request: ChatMessageRequest): IO[ChatMessageResponse] =
    IO {
      toolkit.messageProducer.sendMessage(request)
      ChatMessageResponse()
    }.handleErrorWith(err => IO(ChatMessageResponse(Some(err.getMessage))))

}
