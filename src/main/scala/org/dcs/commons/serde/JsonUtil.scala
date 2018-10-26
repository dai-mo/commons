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

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.{AnnotationIntrospector, DeserializationFeature, ObjectMapper, SerializerProvider}
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
  mapper.setSerializationInclusion(Include.NON_NULL)

  val module: SimpleModule = new SimpleModule()
  module.addSerializer(classOf[org.apache.avro.JsonProperties.Null], new AvroJsonNullSerializer)
  mapper.registerModule(module)


  val introspector: AnnotationIntrospector  = new JaxbAnnotationIntrospector(mapper.getTypeFactory)
  val secondary: AnnotationIntrospector  = new JacksonAnnotationIntrospector
  mapper.setAnnotationIntrospector(AnnotationIntrospector.pair(introspector, secondary))

  def toJson(value: Map[Symbol, Any]): String = {
    toJson(value map { case (k, v) => k.name -> v })
  }

  def toJson(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json: String)(implicit m: Manifest[V]): Map[String, V] =
    toObject[Map[String, V]](json)

  def toList[V](json: String)(implicit m: Manifest[V]): List[V] =
    toObject[List[V]](json)

  def toObject[T](json: String)(implicit m: Manifest[T]): T = {
    mapper.readValue[T](json)
  }

  def toObject[T](is: InputStream)(implicit m: Manifest[T]): T = {
    mapper.readValue[T](is)
  }

  def prettyPrint(json: String)  {
    val mapper: ObjectMapper = new ObjectMapper()
    val prettyJson: Object  = mapper.readValue(json, classOf[Object])
    println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(prettyJson))
  }
}

object JsonPath {
  val Root = "$"
  val Sep = "."
}




class AvroJsonNullSerializer(t: Class[org.apache.avro.JsonProperties.Null])
  extends StdSerializer[org.apache.avro.JsonProperties.Null](t) {

  def this() = this(null)

  def serialize(value: org.apache.avro.JsonProperties.Null,
                jgen:  JsonGenerator,
                provider: SerializerProvider): Unit = {
    jgen.writeNull()
  }
}