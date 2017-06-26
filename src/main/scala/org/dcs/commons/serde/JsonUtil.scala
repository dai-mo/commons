package org.dcs.commons.serde

import java.io.InputStream

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector
import com.fasterxml.jackson.databind.{AnnotationIntrospector, DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
  mapper.setSerializationInclusion(Include.NON_NULL)


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