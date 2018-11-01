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



