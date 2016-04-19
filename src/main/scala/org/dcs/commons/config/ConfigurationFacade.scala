package org.dcs.commons.config

import org.slf4j.LoggerFactory
import org.dcs.commons.yaml.YamlSerializerImplicits._
import java.io.File

class ConfigurationFacade {}

object ConfigurationFacade {
  val Logger = LoggerFactory.getLogger(classOf[ConfigurationFacade]);
  val DefaultConfigFileName = "/config.yaml";
  val ConfigFileKey = "config"

  private val configFilePath = System.getProperty(ConfigFileKey)

  private val configuration = {
    if (configFilePath == null) {
      defaultConfig
    } else {
      customConfig
    }
  }

  def defaultConfig() = {
    val inputStream = this.getClass().getResourceAsStream(DefaultConfigFileName);
    if (inputStream == null) {
      throw new IllegalStateException("Could not load config file");
    } else {
      inputStream.toObject[YamlConfiguration]
    }
  }

  def customConfig() = {
    val configFile = new File(configFilePath);
    Logger.warn("Config file path : " + configFilePath);
    configFile.toObject[YamlConfiguration]
  }
  
  def config = configuration
}

class YamlConfiguration {
  var zookeeperServers: String = "localhost:2181";
}