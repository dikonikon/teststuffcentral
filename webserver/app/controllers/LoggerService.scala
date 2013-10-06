

package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Iteratee, Concurrent}
import akka.actor.{ActorRef, Props, ActorSystem}
import com.teststuffcentral.engine.akka.LoggerActor
import scala.xml.XML._

object LoggerService extends Controller {

  val system = ActorSystem("TestStuffCentralEngine")
  val me = "teststuffweb: "

  def session = WebSocket.using[String] { request => {
      Logger.debug(me + "received WebSocket request")
      var outActor: ActorRef = null
      val out = Concurrent.unicast[String]( channel => {
        Logger.debug(me + "getting new LoggerActor")
        outActor = system.actorOf(Props[LoggerActor])
        Logger.debug(me + "sending channel")
        outActor ! channel
      },
        () => Unit,
        (_, _) => Unit)

      val in = Iteratee.foreach[String]{
        x => {
          Logger.info(me + x)
          val y = loadString(x)
          outActor !  y }
      }.mapDone { _ =>
        Logger.info(me + "Disconnected")
      }
      (in, out)
    }
  }
}
