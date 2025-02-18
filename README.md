# Created by github copilot

# Constructor Workshop Chat Application

## Overview

This project is a chat application built using Scala and SBT. It consists of a client and server component that communicate using Protocol Buffers (ScalaPB). The server handles login, logout, and chat message requests, while the client interacts with the server to perform these actions.

## Project Structure

- `src/main/scala/com/leonteq/workshop/chat/client/ChatClientToolkit.scala`: Contains the client toolkit for handling login, logout, and chat message requests.
- `src/main/scala/com/leonteq/workshop/chat/server/StartConstructorWorkshop.scala`: Main entry point for starting the chat server.
- `src/main/scala/com/leonteq/workshop/chat/server/handlers/LoginRequestHandler.scala`: Handles login requests on the server side.

## Prerequisites

- Scala
- SBT (Scala Build Tool)
- Java Development Kit (JDK)

## Running the Application

### Running the Server

1. Open a terminal and navigate to the project directory.
2. Run the following command to start the server:

    ```sh
    sbt "runMain com.leonteq.workshop.chat.server.StartConstructorWorkshop"
    ```

### Running the Client

1. Open another terminal and navigate to the project directory.
2. Run the following command to start the client:

    ```sh
    sbt "runMain com.leonteq.workshop.chat.client.ChatClientMain"
    ```

You can open multiple terminal windows and run the client command in each to simulate multiple clients.

## Configuration

The server configuration is defined in the `ConstructorWorkshopConfig` class. You can modify the configuration settings as needed.

## Health Checks

The server includes a health check mechanism that monitors the number of online users. If the number of online users exceeds 10, the server is marked as unhealthy.

## Logging

The application uses `StrictLogging` for logging. Logs are generated for various actions such as handling login requests and registering health checks.
