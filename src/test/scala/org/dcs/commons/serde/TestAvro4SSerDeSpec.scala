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

package org.dcs.commons.serde

import java.io.File

import org.dcs.commons.CommonsUnitSpec
import org.dcs.commons.serde.Avro4SImplicits._

class TestAvro4SSerDeSpec extends CommonsUnitSpec {

  val firstName = "Obi"
  val middleName = "Wan"
  val lastName = "Kenobi"
  val age = 1024
  val defaultAge = -1

  case class User(first_name: String, middle_name: String, last_name: String)
  val user = User(firstName, middleName, lastName)

  "Specific Avro Ser De" should "be valid for same reader writer schema when writing to file" in {



    val file = File.createTempFile("user", ".avro")

    List(user).serToFile(file)

    val data = file.deSerToObjects[User]().head

    assert(data.first_name == firstName)
    assert(data.middle_name == middleName)
    assert(data.last_name == lastName)

  }

  "Specific Avro4S Ser De" should "be valid for same reader writer schema when writing to bytes" in {

    val bytes = user.serToBytes()
    val userDeSer= bytes.deSerToObject[User]()

    assert(userDeSer.first_name == firstName)
    assert(userDeSer.middle_name == middleName)
    assert(userDeSer.last_name == lastName)

  }
}
