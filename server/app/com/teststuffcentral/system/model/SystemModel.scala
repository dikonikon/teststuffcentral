package com.teststuffcentral.system.model

import akka.actor.Actor
import akka.event.Logging
import com.teststuffcentral.common.akka.UnexpectedMessageLogging

/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 07/10/13
 * Time: 17:02
 */
class SystemModel extends Actor with UnexpectedMessageLogging {

  val log = Logging(context.system, this)

  def receive = something

  def something: Receive = {
    case x =>
  }

}
