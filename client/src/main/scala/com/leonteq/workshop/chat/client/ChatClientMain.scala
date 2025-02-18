package com.leonteq.workshop.chat.client

import com.leonteq.workshop.chat.client.event.ChatEvent.IncomingChatMessageEvent
import com.leonteq.workshop.chat.client.event.ChatEvent.LoggedIn
import com.leonteq.workshop.chat.client.event.ChatEvent.LoggedOut
import com.leonteq.workshop.chat.client.event.ChatEvent.LoginError
import com.leonteq.workshop.chat.client.event.ChatEvent.OnlineUsersEvent
import com.leonteq.workshop.chat.client.event.ChatEvent.ServiceHealthUpdate
import com.leonteq.workshop.chat.client.event.ChatEvent.TransmissionError
import com.typesafe.scalalogging.StrictLogging
import java.util.Locale
import javax.swing.UIManager
import scala.swing.event.Key
import scala.swing.event.KeyPressed
import swing._
import swing.BorderPanel.Position
import swing.Dialog.Message

object ChatClientMain extends SimpleSwingApplication with StrictLogging {

  private val clientToolkit    = ChatClientToolkit()
  private val chatServerClient = ChatServerClient(clientToolkit)
  private val serverListener   = ChatServerListener(clientToolkit)

  Locale.setDefault(Locale.ENGLISH)
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
  var currentNickname: Option[String] = None

  logger.info("Starting chat client")

  def top: Frame = new Frame {

    title = "Constructor Chat"
    preferredSize = new Dimension(800, 400)

    val chatTextArea      = new TextArea() { editable = false }
    val chatInput         = new TextField()
    val onlineUsersList   = new ListView[String]()
    val serverStatusLabel = new Label()
    serverStatusLabel.icon = Icons.errorIcon
    serverStatusLabel.tooltip = "Awaiting initial server status"

    contents = new BorderPanel {
      val mainPanel = new BorderPanel {
        add(new ScrollPane(chatTextArea), Position.Center)
        add(new ScrollPane(onlineUsersList), Position.East)
      }
      add(mainPanel, Position.Center)
      val chatPanel = new BorderPanel {
        add(chatInput, Position.Center)
        add(serverStatusLabel, Position.East)
      }
      add(chatPanel, Position.South)
    }

    listenTo(chatInput.keys, chatServerClient, serverListener)

    reactions += {
      case KeyPressed(`chatInput`, Key.Enter, _, _) =>
        new javax.swing.SwingWorker[Unit, Unit] {
          def doInBackground(): Unit =
            currentNickname match {
              case Some(nickname) =>
                val message = chatInput.text
                logger.info(s"Sending $message")
                chatServerClient.sendMessage(nickname, message)
              case None           =>
                writeLineToChat(
                  "ERROR: You must be logged in to send messages!"
                )
                askForNickName()
            }

          override def done(): Unit = {
            super.done()
            chatInput.text = ""
          }
        }.execute()

      case LoginError(reason) =>
        Dialog.showMessage(
          chatTextArea,
          s"Reason: ${reason.getOrElse("unknown")}",
          "Login Failure",
          Message.Error
        )
        currentNickname = None
        askForNickName()
      case LoggedIn(nickname) =>
        currentNickname = Some(nickname)
      case LoggedOut(_)       =>
        quit()

      case TransmissionError(reason) =>
        Swing.onEDT {
          writeLineToChat(s"Server ERROR: $reason")
        }

      case IncomingChatMessageEvent(timestamp, nickname, message) =>
        Swing.onEDT {
          writeLineToChat(s"$timestamp $nickname: $message")
        }

      case OnlineUsersEvent(nicknames) =>
        Swing.onEDT {
          onlineUsersList.listData = nicknames
        }

      case ServiceHealthUpdate(health) =>
        if (currentNickname.isEmpty && health.getHealthy) {
          askForNickName()
        }
        Swing.onEDT {
          serverStatusLabel.icon = Icons.getIconForHealth(health)
          serverStatusLabel.tooltip = if (health.hasReason) health.getReason else ""
        }
    }

    centerOnScreen()

    override def closeOperation(): Unit = {
      super.closeOperation()
      currentNickname.foreach(chatServerClient.logout)
    }

    def askForNickName(): Unit =
      new javax.swing.SwingWorker[Option[String], Unit] {
        def doInBackground(): Option[String] =
          Dialog.showInput(chatTextArea, "Enter nickname", initial = "")

        override def done(): Unit =
          get() match {
            case Some(nickname) if nickname.trim.isEmpty => askForNickName()
            case Some(nickname)                          => chatServerClient.login(nickname)
            case None                                    => quit() // user cancelled
          }

      }.execute()

    def writeLineToChat(line: String): Unit = chatTextArea.append(s"$line \n")

    askForNickName()
  }
}
