package com.teststuffcentral.system.model

/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 25/10/13
 * Time: 16:34
 */
case class ModuleStatusChangeEvent(moduleName: String, newStatus: ModuleStatus, oldStatus: ModuleStatus)