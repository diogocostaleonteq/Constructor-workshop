package com.leonteq.workshop.chat.server.service

import java.util.concurrent.ConcurrentLinkedQueue

object UserService {
  val onlineUsers = new ConcurrentLinkedQueue[String]()
}
