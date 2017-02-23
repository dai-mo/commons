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
