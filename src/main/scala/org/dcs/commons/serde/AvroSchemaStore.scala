package org.dcs.commons.serde

import org.apache.avro.Schema

import scala.collection.mutable

/**
  * Created by cmathew on 15.11.16.
  */
object AvroSchemaStore {

  private val SchemaMap = mutable.Map[String, Schema]()

  def add(key: String): Unit = {
    val is = this.getClass.getResourceAsStream("/avro/" + key + ".avsc")
    if(is != null)
      SchemaMap(key) = new Schema.Parser().parse(is)
  }

  def get(key: String): Option[Schema] = {
    SchemaMap.get(key)
  }

  add("org.dcs.core.processor.GBIFOccurrenceProcessor")

}
