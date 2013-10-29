/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 27/08/13
 * Time: 13:44
 */

package com.teststuffcentral.engine.akka

import akka.actor.{Props, ActorSystem, Actor}
import akka.event.Logging

import play.api.libs.iteratee.Concurrent.Channel
import com.teststuffcentral.common.akka.UnexpectedMessageLogging
import com.teststuffcentral.system.model.{ModuleStatusChangeEvent, SystemModel}



class RequestHandlerActor extends Actor with UnexpectedMessageLogging {
  var channel: Channel[AnyRef] = null
  val system = ActorSystem("TestStuffCentralEngine")
  val log = Logging(system, this)

  def receive = expectChannel orElse unexpected

  def expectChannel: Receive = {
    case c: Channel[AnyRef] => {
      log.debug("received channel")
      channel = c
      context.become(expectDeploy orElse unexpected)
    }
  }

  def expectDeploy: Receive = {
    case <deploy>{content}</deploy> => {
      log.debug(content.toString)
      channel.push("received: " + content)
      val runner = system.actorOf(Props[VagrantRunner])
      context.become(expectDeployResult orElse unexpected)
      runner ! <vagrantup>{content}</vagrantup>
    }
  }

  def expectDeployResult: Receive = {
    case <result>{ret}</result> => {
      log.debug("got result: " + ret)
      channel.push(ret.toString)
    }
  }
}

class VagrantRunner extends Actor with UnexpectedMessageLogging {

  import scala.sys.process._

  val log = Logging(context.system, this)
  var status = "inactive"

  def receive = expectStartRequest orElse unexpected

  def expectStartRequest: Receive = {
    case <vagrantup>{path}</vagrantup> => {
      log.debug("received request to run: " + path)
      status = "starting"
      val ret = Process("script/vagrantup.sh").lines.foreach(x => sender ! <result>{x}</result>)
    }
  }
}


class LoggerActor extends Actor {

  import java.io.{File, Writer, FileWriter, PrintWriter, BufferedWriter}
  val log = Logging(context.system, this)
  private var outFile: File = null
  private var out: Writer = null
  private var channel: Channel[AnyRef] = null
  private var nodeId: String = null

  def receive = expectChannel

//    case <stop></stop> => {
//      out.flush()
//      out.close()
//      channel.push("<stopped>" + nodeId + "</stopped>")
//    }

  def expectChannel: Receive = {
    case c: Channel[AnyRef] => {
      log.debug("received channel")
      context.become(expectStartMessage)
      log.debug("changed state to: " + expectStartMessage.getClass.toString)
      channel = c
    }
    case x => log.debug("not expecting message: " + x.toString + " state: " + expectChannel.getClass.toString)
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
          log.debug("changed state to: " + expectLogRecords.getClass.toString)
        }
        case x => log.debug("not expecting message: " + x.toString + " state: " + expectStartMessage.getClass.toString)
      }
    }
  }

  def expectLogRecords: Receive = {
    case <l>{content}</l> => {

      log.debug("logging: " + content)
      out.write(content.text)
      out.write("\n")
      out.flush()
    }
    case x => log.debug("not expecting message: " + x.toString + " state: " + expectLogRecords.getClass.toString)
  }
}

class SystemModelEventActor extends Actor with UnexpectedMessageLogging {

  val log = Logging(context.system, this)
  var model: SystemModel = null

  def receive = expectSystemModel

  def expectSystemModel: Receive = {
    case s: SystemModel => model = s; context.become(expectStatusOrQuery)
  }

  def expectStatusOrQuery = expectStatus orElse expectQuery orElse unexpected

  def expectStatus: Receive = {
    case ModuleStatusChangeEvent(name, newStatus, oldStatus) => {
    }
  }

  def expectQuery: Receive = {
    case x =>
  }
}