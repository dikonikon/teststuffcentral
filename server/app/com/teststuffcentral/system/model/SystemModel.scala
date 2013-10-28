package com.teststuffcentral.system.model

/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 07/10/13
 * Time: 17:02
 */
trait SystemModel {
  def setModuleStatus(name: String, status: ModuleStatus)
  def addComponentByName(name: String, c: Component)
  def addModuleByName(name: String, m: Module)
}
