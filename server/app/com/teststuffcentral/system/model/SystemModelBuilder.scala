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
 * Usage: create an object which extends SystemModelBuilder, and
 */
trait SystemModelBuilderActor extends Actor with UnexpectedMessageLogging {
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

trait ModelBuilder {
  def start: SystemModelBuilder // initialize then create SystemModelBuilder
}

trait SystemModelBuilder {
  def system(name: String): MachineModelBuilder
  def endSystem
}

trait MachineModelBuilder {
  def machine(name: String): TargetModelBuilder
  def endMachine: SystemModelBuilder
}

trait TargetModelBuilder {
  def target(name: String): ModuleModelBuilder
  def endTargets: MachineModelBuilder
}

trait ModuleModelBuilder {
  def module(name: String)
  def endModules: TargetModelBuilder
}

class TestModelBuilder extends ModelBuilder {

  def start = new TestSystemModelBuilder

  class TestSystemModelBuilder extends SystemModelBuilder {
    def system(name: String): MachineModelBuilder = {
      println("system: " + name)
      new TestMachineModelBuilder
    }
    def endSystem = {
      println("end system")
    }
  }

  class TestMachineModelBuilder extends MachineModelBuilder {
    def machine(name: String): TargetModelBuilder = {
      println("machine -> " + name)
      new TestTargetModelBuilder()
    }
    def endMachine = new TestSystemModelBuilder
  }

  class TestTargetModelBuilder extends TargetModelBuilder {
    def target(name: String): ModuleModelBuilder = {
      println("target: " + name)
      new TestModuleModelBuilder
    }
    def endTargets = {
      println("end target")
      new TestMachineModelBuilder
    }
  }

  class TestModuleModelBuilder extends ModuleModelBuilder {
    def module(name: String): ModuleModelBuilder = {
      println("module: " + name)
      new TestModuleModelBuilder
    }
    def endModules = new TestTargetModelBuilder
  }
}


