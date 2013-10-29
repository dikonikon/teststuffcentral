package com.teststuffcentral.system.model

import akka.actor.{Actor, ActorSystem}
import com.teststuffcentral.common.akka.UnexpectedMessageLogging

/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 01/10/13
 * Time: 19:18
 */

/**
 * A SystemModelBuilder builds up an Akka system from a stream of messages.
 * A source of the messages could be a config file parser or DSL.
 */
trait SystemModelBuilder extends Actor with UnexpectedMessageLogging {
  val modelName: String
  var model: ActorSystem

  def receive = expectSystem

  def expectSystem: Receive

  def expectTarget: Receive

  def expectModule: Receive

  def expectEndOfTarget: Receive

  def expectQuery: Receive

  def expectEndOfSystem

}
