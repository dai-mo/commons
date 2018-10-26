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