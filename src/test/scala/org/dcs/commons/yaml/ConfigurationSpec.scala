package org.dcs.commons.yaml

import java.io.File
import org.dcs.commons.serde.YamlSerializerImplicits._
import org.dcs.commons.CommonsUnitSpec
import org.dcs.commons.config.{GlobalConfiguration, GlobalConfigurator}
import org.scalatest.FlatSpec
import org.slf4j.LoggerFactory


class ConfigurationSpec extends CommonsUnitSpec with ConfigurationBehaviors {
  
  System.setProperty("config", this.getClass.getResource("/config.yaml").getPath)
  
  "Loaded Configuration from Default" should
    behave like validConfigurationObject(GlobalConfigurator.defaultConfig().toObject[GlobalConfiguration])
  
  "Loaded Configuration from System Property" should
    behave like validConfigurationObject(GlobalConfigurator.customConfig().toObject[GlobalConfiguration])
  
  "Loaded Configuration" should behave like validConfigurationObject(GlobalConfigurator.config().toObject[GlobalConfiguration])
}

object ConfigurationSpec {
  val logger = LoggerFactory.getLogger(classOf[ConfigurationSpec])
  
  val file = new File(this.getClass.getResource("/config.yaml").toURI())
}

trait ConfigurationBehaviors { this: FlatSpec =>

  def validConfigurationObject(gc: GlobalConfiguration) {
    it should "be valid" in {
      assert(gc.zookeeperServers == "localhost:2282")
    }
  }

}



