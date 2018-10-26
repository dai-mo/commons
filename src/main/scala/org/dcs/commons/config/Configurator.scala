/*
 * Copyright (c) 2017-2018 brewlabs SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dcs.commons.config

import org.slf4j.LoggerFactory
import java.io.{File, FileInputStream}

import scala.util.control.NonFatal

class Configurator(defaultConfigFilePath: Option[String],
                   customConfigFilePropertyKey: Option[String]) {

  val logger = LoggerFactory.getLogger(classOf[Configurator])


  def defaultConfig() = {
    if (defaultConfigFilePath.isEmpty)
      throw new IllegalStateException("No default config file path specified")
    val inputStream = this.getClass().getResourceAsStream(defaultConfigFilePath.get)
    if (inputStream == null)
      throw new IllegalStateException("Could not load config file")
    inputStream
  }

  def customConfig() = {
    if (customConfigFilePropertyKey.isEmpty)
      throw new IllegalStateException("No custom config file property specified")
    val configFilePath = System.getProperty(customConfigFilePropertyKey.get)
    val configFile = new File(configFilePath)
    if(!configFile.exists())
      throw new IllegalStateException("Custom config file does not exist")
    logger.warn("Config file path : " + configFilePath)
    new FileInputStream(configFile)
  }

  def config() = {
    try {
      customConfig()
    } catch {
      case NonFatal(t) => defaultConfig()
    }

  }


}

object Configurator {


  def apply(defaultConfigFilePath: String,
            customConfigFilePropertyKey: String): Configurator =
    new Configurator(Some(defaultConfigFilePath),
      Some(customConfigFilePropertyKey))

  def apply(configKey: String): Configurator =
    new Configurator(Some("/" + configKey + ".yaml"), Some(configKey))
}

object GlobalConfigurator extends Configurator(Some("/config.yaml"), Some("config"))



class GlobalConfiguration {
  var zookeeperServers = "localhost:2181"
  var nifiBaseUrl = "http://localhost:8888/nifi-api"
}