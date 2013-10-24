package com.teststuffcentral.system.model

/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 01/10/13
 * Time: 19:18
 */
trait SystemModelBuilder {

  val model = implicitly[SystemModel]

  def configure()

  def component(name: String) = {
    model.addComponentByName(name, Component(name))
  }
  def onMachine(m: Machine)
  def connectsTo(c: Component)
  def atAddress(a: Address)
  def discoveredAt(s: AddressSource)
}
