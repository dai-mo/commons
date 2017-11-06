package org.dcs.commons.serde

import java.io.{File, InputStream}

object YamlSerializerImplicits {
  implicit class StringToObject(yamlString: String) {
    def asList[V]()(implicit m: Manifest[V]): List[V] = YamlUtil.toList[V](yamlString)
    def toMap: Map[String,Any] = YamlUtil.toMap(yamlString)
    def toMapOf[V]()(implicit m: Manifest[V]): Map[String,V] = YamlUtil.toMap[V](yamlString)
    def toObject[T]()(implicit m: Manifest[T]): T =  YamlUtil.toObject[T](yamlString)    
  }
  
  implicit class InputStreamToObject(yamlInputStream: InputStream) {
    def toObject[T]()(implicit m: Manifest[T]): T =  YamlUtil.toObject[T](yamlInputStream)    
  }
  
  implicit class FileToObject(yamlFile: File) {
    def toObject[T]()(implicit m: Manifest[T]): T =  YamlUtil.toObject[T](yamlFile)    
  }

  implicit class ObjectToString[T](yamlObject: T) {
    def toYaml: String = YamlUtil.toYaml(yamlObject)
  }
}