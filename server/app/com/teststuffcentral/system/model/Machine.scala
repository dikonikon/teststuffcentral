package com.teststuffcentral.system.model

/**
 * See: https://github.com/dikonikon
 * This is open source software provided under the license
 * at the root directory of the project
 * Date: 02/10/13
 * Time: 14:42
 */
case class Machine(name: String) {

  private var targets = Map[String, Target]()

  def +(target: Target): Unit = {
    targets = targets + (target.name -> target)
  }
}
