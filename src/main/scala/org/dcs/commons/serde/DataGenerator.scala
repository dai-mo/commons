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
