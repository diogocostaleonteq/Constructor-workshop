package com.leonteq.workshop.chat.client

import com.leonteq.service.messages.Service.ServiceHealth
import com.leonteq.service.messages.Service.ServiceHealth.Health
import java.awt.Toolkit
import javax.swing.ImageIcon

object Icons {
  val toolkit     = Toolkit.getDefaultToolkit
  val okIcon      = iconForName("accept.png")
  val errorIcon   = iconForName("exclamation.png")
  val warningIcon = iconForName("error.png")

  private def iconForName(name: String): ImageIcon =
    new ImageIcon(
      toolkit.getImage(
        Thread.currentThread().getContextClassLoader.getResource(name)
      )
    )

  def getIconForHealth(health: ServiceHealth): ImageIcon =
    if (health.hasHealth) {
      health.getHealth match {
        case Health.HEALTHY   => okIcon
        case Health.UNHEALTHY => warningIcon
        case Health.DEAD      => errorIcon
      }
    } else {
      if (health.getHealthy) okIcon else warningIcon
    }
}
