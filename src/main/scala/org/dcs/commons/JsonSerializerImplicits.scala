package org.dcs.commons

import java.io.InputStream

object JsonSerializerImplicits {
  implicit class StringToObject(jsonString: String) {
    def toMap: Map[String,Any] = JsonUtil.toMap(jsonString)
    def toMapOf[V]()(implicit m: Manifest[V]): Map[String,V] = JsonUtil.toMap[V](jsonString)
    def toObject[T]()(implicit m: Manifest[T]): T =  JsonUtil.toObject[T](jsonString)
    def toJsonP = JsonUtil.prettyPrint(jsonString)
  }
  
  implicit class InputStreamToObject(jsonInputStream: InputStream) {
    def toObject[T]()(implicit m: Manifest[T]): T =  JsonUtil.toObject[T](jsonInputStream)
  }

  implicit class ObjectToString(jsonObject: Any) {
    def toJson: String = JsonUtil.toJson(jsonObject)
    def toJsonP = JsonUtil.prettyPrint(JsonUtil.toJson(jsonObject))
  }
}