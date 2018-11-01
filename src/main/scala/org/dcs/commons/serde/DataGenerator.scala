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

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericRecord, GenericRecordBuilder}
import org.dcs.commons.serde.AvroImplicits._

object DataGenerator {

  val PersonSchemaId = "org.dcs.test.Person"
  val PersonSchema: Option[Schema] = AvroSchemaStore.get(PersonSchemaId)

  def persons(noOfRecords: Int): List[GenericRecord] = Range.inclusive(1,noOfRecords).map(i => new GenericRecordBuilder(PersonSchema.get)
    .set("name", "Person" + i)
    .set("age", i)
    .set("gender", if(i % 2 == 0) "female" else "male")
    .build()).toList

  def personsSer(noOfRecords: Int): List[Array[Byte]] = persons(noOfRecords).map(_.serToBytes(PersonSchema))

}
