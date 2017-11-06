package org.dcs.commons.serde

import java.io.InputStream

object JsonSerializerImplicits {
  implicit class StringToObject(jsonString: String) {
    def asList[V]()(implicit m: Manifest[V]): List[V] = JsonUtil.toList[V](jsonString)
    def toMap: Map[String,Any] = JsonUtil.toMap(jsonString)
    def toMapOf[V]()(implicit m: Manifest[V]): Map[String,V] = JsonUtil.toMap[V](jsonString)
    def toObject[T]()(implicit m: Manifest[T]): T =  JsonUtil.toObject[T](jsonString)
    def toJsonP(): Unit = JsonUtil.prettyPrint(jsonString)
  }
  
  implicit class InputStreamToObject(jsonInputStream: InputStream) {
    def toObject[T]()(implicit m: Manifest[T]): T =  JsonUtil.toObject[T](jsonInputStream)
  }

  implicit class ObjectToString(jsonObject: Any) {
    def toJson: String = JsonUtil.toJson(jsonObject)
    def toJsonP(): Unit = JsonUtil.prettyPrint(JsonUtil.toJson(jsonObject))
  }

}