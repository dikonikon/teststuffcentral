
/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 24/10/13
 * Time: 12:40
 */

package com.teststuffcentral.common.akka

import akka.actor.Actor._
import play.api.Logger


trait UnexpectedMessageLogging {
  val me: String
  def unexpected: Receive = {
    case x => {
      Logger.error(me + "received: " + x + " but don't know what to do with it")
      Logger.error(me + "type of message received was: " + x.getClass.toString)
    }
  }
}
