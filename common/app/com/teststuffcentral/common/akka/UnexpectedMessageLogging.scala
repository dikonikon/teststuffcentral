
/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 24/10/13
 * Time: 12:40
 */

package com.teststuffcentral.common.akka

import akka.actor.Actor
import akka.event.{LoggingAdapter}


trait UnexpectedMessageLogging {
  self: Actor =>
  val log: LoggingAdapter
  def unexpected: Receive = {
    case x => {
      log.error("received: " + x + " but don't know what to do with it")
      log.error("type of message received was: " + x.getClass.toString)

    }
  }
}


