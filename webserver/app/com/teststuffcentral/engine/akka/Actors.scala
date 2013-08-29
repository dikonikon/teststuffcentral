/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 27/08/13
 * Time: 13:44
 */

package com.teststuffcentral.engine.akka

import akka.actor.{Props, ActorSystem, Actor}
import play.api.Logger
import play.api.libs.iteratee.Concurrent.Channel
import scala.xml._


class RequestHandlerActor extends Actor {
  var channel: Channel[AnyRef] = null
  val system = ActorSystem("TestStuffCentralEngine")
  val me = "requesthandler: "

  def receive = {
    case c: Channel[AnyRef] => {
      Logger.debug(me + "received channel")
      channel = c
    }
    case <deploy>{content}</deploy> => {
      Logger.debug( me + content.toString)
      channel.push("received: " + content)
      val runner = system.actorOf(Props[VagrantRunner])
      runner ! <vagrantup>{content}</vagrantup>
    }
    case <result>{ret}</result> => {
      Logger.debug(me + "got result: " + ret)
      channel.push(ret.toString)
    }
    case x => {
      Logger.debug(me + "received message, but don't know what to do with it: " + x)
      Logger.debug(me + "type of message is: " + x.getClass.toString)
      val y: Elem = x.asInstanceOf[Elem]
      Logger.debug(me + "child is: " + y.child)
    }
  }
}

class VagrantRunner extends Actor {

  import scala.sys.process._

  val me = "vagrantrunner: "
  var status = "inactive"

  def receive = {
    case <vagrantup>{path}</vagrantup> => {
      Logger.debug(me + "received request to run: " + path)
      status = "starting"
      val ret = Process("ls").lines.fold("")((x, y) => x + ", " + y)
      sender ! <result>{ret}</result>
    }
  }
}
