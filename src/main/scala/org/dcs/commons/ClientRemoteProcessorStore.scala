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
}
