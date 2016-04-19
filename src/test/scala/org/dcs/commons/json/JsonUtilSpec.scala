package org.dcs.commons.json

import org.slf4j.LoggerFactory
import org.dcs.commons.CommonsBaseUnitSpec
import org.scalatest.FlatSpec
import org.dcs.commons.json.JsonSerializerImplicits._

class JsonUtilSpec extends CommonsBaseUnitSpec with JsonUtilBehaviors {
  
  "Loaded Json Object from String" should behave like validJsonObject(JsonUtilSpec.jsonString.toObject[TestJsonObject])
  
  "Loaded Json Object from InputStream" should behave like validJsonObject(JsonUtilSpec.is.toObject[TestJsonObject])
  
  "Loaded Json String" should behave like validJsonString(JsonUtilSpec.jsonObject.toJson)

}

object JsonUtilSpec {
  val logger = LoggerFactory.getLogger(classOf[JsonUtilSpec])
  
  val Valuea = "valuea"

  val jsonString = "{\"a\":\"" + Valuea + "\"}"
  
  val jsonObject = new TestJsonObject
  jsonObject.a = Valuea
  
  val is = this.getClass.getResourceAsStream("/test.json")
}

trait JsonUtilBehaviors { this: FlatSpec =>

  def validJsonObject(jo: TestJsonObject) {
    it should "be valid" in {
      assert(jo.a == JsonUtilSpec.Valuea)
    }
  }
  
  def validJsonString(js: String) {
    it should "be valid" in {
      assert(js == JsonUtilSpec.jsonString)
    }
  }
}

class TestJsonObject() {
  var a: String = _

}