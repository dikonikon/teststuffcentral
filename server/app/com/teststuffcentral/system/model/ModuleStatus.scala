package com.teststuffcentral.system.model

/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 25/10/13
 * Time: 16:36
 */
sealed case class ModuleStatus(statusName: String) {
  override def toString = this.statusName
}

object ModuleStatus {
  val Unknown = ModuleStatus("unknown")
  val Uninstalled = ModuleStatus("uninstalled")
  val Installed = ModuleStatus("installed")
  val Resolved = ModuleStatus("resolved")
  val Starting = ModuleStatus("starting")
  val Started = ModuleStatus("active")
  val Stopping = ModuleStatus("stopping")
}
