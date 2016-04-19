package org.dcs.commons.yaml

import org.slf4j.LoggerFactory
import org.dcs.commons.CommonsBaseUnitSpec
import org.scalatest.FlatSpec
import org.dcs.commons.yaml.YamlSerializerImplicits._
import java.io.File
import org.dcs.commons.config.YamlConfiguration
import org.dcs.commons.config.ConfigurationFacade


class ConfigurationSpec extends CommonsBaseUnitSpec with ConfigurationBehaviors {
  
  System.setProperty("config", this.getClass.getResource("/config.yaml").getPath)
  
  "Loaded Configuration from Default" should behave like validConfigurationObject(ConfigurationFacade.defaultConfig)
  
  "Loaded Configuration from System Property" should behave like validConfigurationObject(ConfigurationFacade.customConfig)
  
  "Loaded Configuration" should behave like validConfigurationObject(ConfigurationFacade.config)
}

object ConfigurationSpec {
  val logger = LoggerFactory.getLogger(classOf[ConfigurationSpec])
  
  val file = new File(this.getClass.getResource("/config.yaml").toURI())
}

trait ConfigurationBehaviors { this: FlatSpec =>

  def validConfigurationObject(yc: YamlConfiguration) {
    it should "be valid" in {
      assert(yc.zookeeperServers == "localhost:2282")
    }
  }

}



