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

package org.dcs.commons

import scala.collection.mutable

/**
  * Created by cmathew on 15.11.16.
  */
object ClientRemoteProcessorStore {

  private val ProcessorMap = mutable.Map[String, String]()

  def add(key: String, value: String) =  {
    ProcessorMap(key) = value
  }

  def get(key: String): Option[String] = {
    ProcessorMap.get(key)
  }

  add("org.dcs.nifi.processors.StatefulGBIFOccurrenceProcessor", "org.dcs.core.processor.GBIFOccurrenceProcessor")
  add("org.dcs.nifi.processors.LatLongValidationProcessor", "org.dcs.core.processor.GBIFOccurrenceProcessor")
}
