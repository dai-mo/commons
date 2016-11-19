package org.dcs.commons.error

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.commons.serde.AvroSchemaStore

/**
  * Created by cmathew on 05/06/16.
  */

case class ErrorResponse(code: String,
                         message: String,
                         httpStatusCode: Int,
                         var errorMessage: String = "") {
  def withErrorMessage(em: String): ErrorResponse  = {
    this.errorMessage = em
    this
  }

  def avroRecord(): GenericRecord = {
    val record = new GenericData.Record(AvroSchemaStore.errorResponseSchema())
    record.put("code", code)
    record.put("message", message)
    record.put("httpStatusCode", httpStatusCode)
    record.put("errorMessage", errorMessage)
    record
  }
}

class RESTException(val errorResponse: ErrorResponse)
  extends Exception(errorResponse.code + ":" +
    errorResponse.httpStatusCode + ":" +
    errorResponse.message)
