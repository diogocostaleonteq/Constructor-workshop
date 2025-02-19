package com.leonteq.workshop.chat.server

import com.leonteq.service.modules.LeonteqMessageProducer
import com.leonteq.service.modules.LeonteqServerToolkit
import javax.inject.Singleton

@Singleton
class ChatServerToolkit extends LeonteqServerToolkit {

  override def serviceName: String = "constructor-workshop"

  val messageProducer: LeonteqMessageProducer = createLeonteqMessageProducer

}
