package org.dcs.commons.yaml

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.InputStream
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.io.File

object YamlUtil {
  val mapper = new ObjectMapper(new YAMLFactory()) with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

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
  
  def toObject[T](is: InputStream)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](is)
  }
  
  def toObject[T](file: File)(implicit m : Manifest[T]): T = {
    mapper.readValue[T](file)
  }
}