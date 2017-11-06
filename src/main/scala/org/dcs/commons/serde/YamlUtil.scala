package org.dcs.commons.serde

import java.io.{File, InputStream}

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object YamlUtil {
  val mapper = new ObjectMapper(new YAMLFactory()) with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.setSerializationInclusion(Include.NON_NULL)


  def toYaml(value: Map[Symbol, Any]): String = {
    toYaml(value map { case (k,v) => k.name -> v})
  }

  def toYaml(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](yaml:String)(implicit m: Manifest[V]) = toObject[Map[String,V]](yaml)

  def toObject[T](yaml: String)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](yaml)
  }

  def toList[V](json: String)(implicit m: Manifest[V]): List[V] =
    toObject[List[V]](json)
  
  def toObject[T](is: InputStream)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](is)
  }
  
  def toObject[T](file: File)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](file)
  }
}