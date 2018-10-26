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
    if(!SchemaMap.contains(key))
      add(key)
    SchemaMap.get(key)
  }

  add(ErrorResponseSchemaId)

  def errorResponseSchema(): Schema = {
    SchemaMap(ErrorResponseSchemaId)
  }
}
