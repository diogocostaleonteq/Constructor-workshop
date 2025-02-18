package com.leonteq.workshop.chat.server

import com.leonteq.service.modules.LeonteqMessageProducer
import com.leonteq.service.modules.LeonteqServerToolkit
import javax.inject.Singleton
import scala.collection.mutable.ArrayBuffer

@Singleton
class ChatServerToolkit extends LeonteqServerToolkit {

  override def serviceName: String = "constructor-workshop"

  val onlineUsers: ArrayBuffer[String] = ArrayBuffer[String]()

  val messageProducer: LeonteqMessageProducer = createLeonteqMessageProducer

}
