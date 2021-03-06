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

package org.dcs.commons.json

import org.slf4j.LoggerFactory
import org.dcs.commons.CommonsUnitSpec
import org.dcs.commons.serde.JsonSerializerImplicits._
import org.scalatest.FlatSpec

import scala.beans.BeanProperty

class JsonUtilSpec extends CommonsUnitSpec with JsonUtilBehaviors {
  import JsonUtilSpec._
  
  "Loaded Json Object from String" should behave like validJsonObject(JsonUtilSpec.jsonString.toObject[TestJsonObject])
  
  "Loaded Json Object from InputStream" should behave like validJsonObject(JsonUtilSpec.is.toObject[TestJsonObject])
  
  "Loaded Json String" should behave like validJsonString(JsonUtilSpec.jsonObject.toJson)

  "List of Strings Conversion" should behave like validListOfStringsConversion(ls.toJson)

  "List of Objects Conversion" should behave like validListOfObjectsConversion(lsObjs.toJson)

}

case class TestObject(@BeanProperty var str: String) {
  def this() = this("")
}

object JsonUtilSpec {
  val logger = LoggerFactory.getLogger(classOf[JsonUtilSpec])

  val Valuea = "valuea"

  val jsonString = "{\"a\":\"" + Valuea + "\"}"

  val jsonObject = TestJsonObject(Valuea)

  val is = this.getClass.getResourceAsStream("/test.json")

  val ls = List("first", "second")

  val lsObjs = ls.map(s => TestObject(s))
}

trait JsonUtilBehaviors { this: FlatSpec =>

  import JsonUtilSpec._

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

  def validListOfStringsConversion(lsString: String): Unit = {
    it should "be valid" in {
      assert(ls == lsString.asList[String])
    }
  }

  def validListOfObjectsConversion(lsString: String): Unit = {
    it should "be valid" in {
      assert(lsObjs == lsString.asList[TestObject])
    }
  }
}

case class TestJsonObject(@BeanProperty var a: String) {
  def this() = this("")
}