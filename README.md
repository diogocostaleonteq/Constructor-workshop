# Constructor Workshop Chat Application

## Overview

This project is a chat application built using Scala and SBT. 
It consists of a client and server component that communicate using Protocol Buffers (ScalaPB). 
The server handles login, logout, and chat message requests, while the client interacts with the server to perform these actions.

## Project Structure

- `src/main/scala/com/leonteq/workshop/chat/client/ChatClientToolkit.scala`: Contains the client toolkit for handling login, logout, and chat message requests.
- `src/main/scala/com/leonteq/workshop/chat/client/ChatClientMain.scala`: Main entry point for starting the chat client.
- `src/main/scala/com/leonteq/workshop/chat/server/ChatServerToolkit.scala`: Contains the server toolkit for handling login, logout, and chat message requests.
- `src/main/scala/com/leonteq/workshop/chat/server/StartConstructorWorkshop.scala`: Main entry point for starting the chat server.
- `src/main/scala/com/leonteq/workshop/chat/server/handlers`: Where the Login/Logout/ChatMessage handlers are implemented.

## Prerequisites

- Scala
- SBT (Scala Build Tool)
- Java Development Kit (JDK)