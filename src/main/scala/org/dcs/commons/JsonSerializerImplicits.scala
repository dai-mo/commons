package org.dcs.commons

import java.io.InputStream

object JsonSerializerImplicits {
  implicit class StringToObject(jsonString: String) {
    def toMap: Map[String,Any] = JsonUtil.toMap(jsonString)
    def toMapOf[V]()(implicit m: Manifest[V]): Map[String,V] = JsonUtil.toMap[V](jsonString)
    def toObject[T]()(implicit m: Manifest[T]): T =  JsonUtil.toObject[T](jsonString)
  }
  
  implicit class InputStreamToObject(yamlInputStream: InputStream) {
    def toObject[T]()(implicit m: Manifest[T]): T =  JsonUtil.toObject[T](yamlInputStream)
  }

  implicit class ObjectToString[T](jsonObject: T) {
    def toJson: String = JsonUtil.toJson(jsonObject)
  }
}