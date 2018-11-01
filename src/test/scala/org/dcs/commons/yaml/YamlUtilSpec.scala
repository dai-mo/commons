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

import org.slf4j.LoggerFactory
import org.dcs.commons.CommonsUnitSpec
import org.scalatest.FlatSpec
import org.dcs.commons.serde.YamlSerializerImplicits._
import java.io.File


class YamlUtilSpec extends CommonsUnitSpec with YamlUtilBehaviors {
  
  "Loaded Yaml Object from String" should behave like validYamlObject(YamlUtilSpec.yamlString.toObject[TestYamlObject])
  
  "Loaded Yaml Object from Input Stream" should behave like validYamlObject(YamlUtilSpec.is.toObject[TestYamlObject])
  
  "Loaded Yaml Object from File" should behave like validYamlObject(YamlUtilSpec.file.toObject[TestYamlObject])
  
  "Loaded Yaml String" should behave like validYamlString(YamlUtilSpec.yamlObject.toYaml)

}

object YamlUtilSpec {
  val logger = LoggerFactory.getLogger(classOf[YamlUtilSpec])
  
  val Valuea = "valuea"

  val yamlString = "---\na: \"" + Valuea + "\"\n"
  
  val yamlObject = new TestYamlObject
  yamlObject.a = Valuea
  
  val is = this.getClass.getResourceAsStream("/test.yaml")
  
  val file = new File(this.getClass.getResource("/test.yaml").toURI())
}

trait YamlUtilBehaviors { this: FlatSpec =>

  def validYamlObject(jo: TestYamlObject) {
    it should "be valid" in {
      assert(jo.a == YamlUtilSpec.Valuea)
    }
  }
  
  def validYamlString(js: String) {
    it should "be valid" in {     
      assert(js == YamlUtilSpec.yamlString)
    }
  }
}

class TestYamlObject() {
  var a: String = _
}


