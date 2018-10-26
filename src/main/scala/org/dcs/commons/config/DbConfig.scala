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

package org.dcs.commons.config

import java.util.Properties

/**
  * Created by cmathew on 02.02.17.
  */
object DbConfig {
  val DbPropertyFileName = "application.properties"
  val DbPostgresPrefix = "postgres"

  val ComponentIdField = "component_id"


  val properties : Properties  = {
    val dbProperties = new Properties()
    val dbPropertiesIS = getClass.getClassLoader.getResourceAsStream(DbPropertyFileName)
    if(dbPropertiesIS != null)
      dbProperties.load(dbPropertiesIS)
    else
      throw new IllegalStateException("Could not load properties from file : " + DbPropertyFileName)
    dbProperties
  }
}
