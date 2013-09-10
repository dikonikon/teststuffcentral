/**
 * See: https://github.com/dikonikon
 * This software is provided under the license
 * at the root directory of the project
 * Date: 06/09/13
 * Time: 13:57
 */

import play.api.GlobalSettings
import play.api.Application
import play.api.Logger

import com.typesafe.config._
import com.teststuffcentral.client.RemoteTail


object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("starting logstreaming app...")
    val conf = ConfigFactory.load()
    val env = conf.getString("teststuffcentral.environment")
    val serverUrl = conf.getString(env + ".logserver.url")
    val nodeId = conf.getString(env + ".nodeid")
    val logFilePath = conf.getString(env + "." + nodeId + ".logfilepath")
    RemoteTail.start(serverUrl, logFilePath, env, nodeId)
    Logger.info("logstreaming for node: " + nodeId + " in environment: " + env + " started")
  }

}
