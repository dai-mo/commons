package org.dcs.commons.yaml

import org.slf4j.LoggerFactory
import org.dcs.commons.CommonsBaseUnitSpec
import org.scalatest.FlatSpec
import org.dcs.commons.YamlSerializerImplicits._
import java.io.File


class YamlUtilSpec extends CommonsBaseUnitSpec with YamlUtilBehaviors {
  
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


