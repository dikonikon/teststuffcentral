/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 06/09/13
 * Time: 15:35
 */

package com.teststuffcentral.client

import mojolly.io._
// import com.osinka.tailf._
import java.io.{InputStream, File}
import mojolly.io.WebSocketClient.Messages.{Connected, Disconnected, TextMessage}
import java.net.URI
import play.api._
import akka.actor.{Props, ActorSystem, Actor, ActorRef}


import scala.io.Source
import scala.concurrent.Future
import scala.xml.XML


object RemoteTail {

  val me = "remotetail: "

  def start(serverUrl: String, filePath: String, environment: String, nodeId: String) {
    val actorSystem = ActorSystem("TestStuffCentralClient")
    // val instream = Tail.follow(new File(filePath)) this will be useful as we test this on logs that roll over
    val senderActor = actorSystem.actorOf(Props[LogSenderActor])

    WebSocketClient(new URI(serverUrl)) {
      case Connected(client) => {
        Logger.debug(me + "connected to server")
        senderActor ! (environment, nodeId, filePath)
        senderActor ! client
      }
      case Disconnected(client, _) => {
        Logger.debug(me + client.url.toASCIIString + " disconnected.")
        // todo need to halt outgoing until reconnected - ignore for first case
        // todo maybe a case to clean up actor etc if/when the server ends the session
      }
      case TextMessage(client, message) => {
        Logger.debug(me + "received: " + message)
        senderActor ! message
      }
    }.connect
  }
}

class LogSenderActor extends Actor {

  var webSocket: WebSocketClient = null
  var environment: String = null
  var nodeId: String = null
  var filePath: String = null
  val me = "logsenderactor: "

  import context.dispatcher

  def receive = expectingContextData

  def expectingContextData: Receive = {
    case (env: String, nodeId: String, filePath: String) => {
      this.environment = env
      this.nodeId = nodeId
      this.filePath = filePath
      context.become(expectWebSocket)
      Logger.debug(me + "changed state to: " + expectWebSocket.getClass.toString)
    }
    case x => Logger.debug(me + "not expecting message: " + x + " state: " + expectingContextData)

  }

  def expectWebSocket: Receive = {
    case s: WebSocketClient => {
      this.webSocket = s
      Logger.debug(me + "received websocket")
      val message = "<start><environment>" + this.environment + "</environment>" +
        "<nodeid>" + this.nodeId + "</nodeid></start>"
      context.become(expectStarted)
      Logger.debug(me + "changed state to: " + expectStarted)
      Logger.debug(me + "sending: " + message)
      webSocket.send(message)
    }
    case x => Logger.debug(me + "not expecting message: " + x + " state: " + expectWebSocket.getClass.toString)

  }

  def expectStarted: Receive = {
    case s: String => {
      val xmlMessage = XML.loadString(s)
      xmlMessage match {
        case <started>{content}</started> => {
          Logger.debug(me + "received started")
          context.become(expectLogRecord)
          Logger.debug(me + "changed state to: " + expectLogRecord)
          // todo close off etc with the following
          val logFileRead = Future {
            for (line <- Source.fromFile(filePath).getLines()) self ! line
          }
        }
      }
    }
    case x => Logger.debug(me + "not expecting message: " + x + " state: " + expectStarted.getClass.toString)
  }

  def expectLogRecord: Receive = {
    case logRecord: String  => {
      Logger.debug(me + "sending: " + logRecord)
      webSocket.send("<l>" + logRecord + "</l>")
    }
    case x => Logger.debug(me + "not expecting message: " + x + " state: " + expectLogRecord.getClass.toString)
  }
}


