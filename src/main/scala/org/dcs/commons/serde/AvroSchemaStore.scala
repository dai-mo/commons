package org.dcs.commons.serde

import java.io.InputStream

import org.apache.avro.Schema

import scala.collection.mutable
import org.dcs.commons.Control._
import org.slf4j.{Logger, LoggerFactory}


/**
  * Created by cmathew on 15.11.16.
  */
object AvroSchemaStore {
  val logger: Logger = LoggerFactory.getLogger(AvroSchemaStore.getClass)

  private val SchemaMap = mutable.Map[String, Schema]()

  val ErrorResponseSchemaId = "org.dcs.commons.error.ErrorResponse"

  def add(key: String): Unit = {
    val is = this.getClass.getResourceAsStream("/avro/" + key + ".avsc")
    if (is == null)
      logger.warn("Cannot load schema with id " + key + ". Schema does not exist")
    else
    using(is) { is =>
      SchemaMap(key) = new Schema.Parser().parse(is)
    }
  }

  def add(key: String, is: InputStream): Unit = {
    if (is == null)
      logger.warn("Cannot load schema with id " + key + ". Null schema")
    else
      using(is) { is =>
        SchemaMap(key) = new Schema.Parser().parse(is)
      }
  }

  def get(key: String): Option[Schema] = {
    SchemaMap.get(key)
  }

  add(ErrorResponseSchemaId)

  def errorResponseSchema(): Schema = {
    SchemaMap(ErrorResponseSchemaId)
  }
}
