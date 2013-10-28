

/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 25/10/13
 * Time: 17:10
 */

package com.teststuffcentral.system.model

case class Target(name: String) {
  private var modules = Map[String, Module]()

  def +(m: Module): Unit = {
    modules = modules + (m.name -> m)
  }

}
