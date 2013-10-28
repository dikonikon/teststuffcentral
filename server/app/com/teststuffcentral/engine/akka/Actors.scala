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
import com.teststuffcentral.common.akka.UnexpectedMessageLogging
import com.teststuffcentral.system.model.{ModuleStatusChangeEvent, SystemModel}



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
      val ret = Process("script/vagrantup.sh").lines.foreach(x => sender ! <result>{x}</result>)
    }
  }
}

class LoggerActor extends Actor {

  import java.io.{File, Writer, FileWriter, PrintWriter, BufferedWriter}

  private var outFile: File = null
  private var out: Writer = null
  private var channel: Channel[AnyRef] = null
  private val me = "loggeractor: "
  private var nodeId: String = null

  def receive = expectChannel

//    case <stop></stop> => {
//      out.flush()
//      out.close()
//      channel.push("<stopped>" + nodeId + "</stopped>")
//    }

  def expectChannel: Receive = {
    case c: Channel[AnyRef] => {
      Logger.debug(me + "received channel")
      context.become(expectStartMessage)
      Logger.debug(me + "changed state to: " + expectStartMessage.getClass.toString)
      channel = c
    }
    case x => Logger.debug(me + "not expecting message: " + x.toString + " state: " + expectChannel.getClass.toString)
  }

  def expectStartMessage: Receive = {
    case <start>{content @ _*}</start> => {
      content match {
        case List(a, b) => {
          val environmentName = a.text
          nodeId = b.text
          val dir = new File("logstore" + File.separatorChar + environmentName)
          if (!dir.exists) {
            dir.mkdir()
          }
          outFile = new File(dir.getPath + File.separatorChar + nodeId)
          if (!outFile.exists) {
            outFile.createNewFile
          }
          out = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))
          context.become(expectLogRecords)
          channel.push("<started>" + nodeId + "</started>")
          Logger.debug(me + "changed state to: " + expectLogRecords.getClass.toString)
        }
        case x => Logger.debug(me + "not expecting message: " + x.toString + " state: " + expectStartMessage.getClass.toString)
      }
    }
  }

  def expectLogRecords: Receive = {
    case <l>{content}</l> => {

      Logger.debug(me + "logging: " + content)
      out.write(content.text)
      out.write("\n")
      out.flush()
    }
    case x => Logger.debug(me + "not expecting message: " + x.toString + " state: " + expectLogRecords.getClass.toString)
  }
}

class SystemModelEventActor extends Actor with UnexpectedMessageLogging {

  val me = "statusreceiveractor: "
  var model: SystemModel = null

  def receive = expectSystemModel

  def expectSystemModel: Receive = {
    case s: SystemModel => model = s; context.become(expectStatusOrQuery)
  }

  def expectStatusOrQuery = expectStatus orElse expectQuery orElse unexpected

  def expectStatus: Receive = {
    case ModuleStatusChangeEvent(name, newStatus, oldStatus) => {
      model.setModuleStatus(name, newStatus)
    }
  }

  def expectQuery: Receive = {
    case x =>
  }
}
