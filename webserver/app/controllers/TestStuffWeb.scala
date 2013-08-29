

package controllers

import play.api._
import play.api.mvc._
import play.api.libs.iteratee.{Iteratee, Concurrent}
import akka.actor.{ActorRef, Props, ActorSystem}
import com.teststuffcentral.engine.akka.RequestHandlerActor
import scala.xml.XML._

object TestStuffWeb extends Controller {

  val system = ActorSystem("TestStuffCentralEngine")
  val me = "session: "

  def session = WebSocket.using[String] { request => {
      Logger.debug("received WebSocket request")
      var outActor: ActorRef = null
      val out = Concurrent.unicast[String]( channel => {
        Logger.debug(me + "getting new RequestHandlerActor")
        outActor = system.actorOf(Props[RequestHandlerActor])
        Logger.debug(me + "sending channel")
        outActor ! channel
      },
        () => Unit,
        (_, _) => Unit)
      val in = Iteratee.foreach[String]{
        x => {
          Logger.info("session:" + x)
          val y = loadString(x)
          outActor !  y }
      }.mapDone { _ =>
        Logger.info(me + "Disconnected")
      }
      (in, out)
    }
  }
}
