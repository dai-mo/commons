package org.dcs.commons.error

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.commons.serde.AvroSchemaStore

/**
  * Note that since case classes cannot inherit from other case classes
  * we need to duplicate error responses and ensure that the variable names
  * are consistent.
  *
  * Created by cmathew on 05/06/16.
  */



case class ErrorResponse(code: String,
                         message: String,
                         var description: String = "") {
  def withDescription(em: String): ErrorResponse = {
    this.description = em
    this
  }

  def avroRecord(): GenericRecord = {
    val record = new GenericData.Record(AvroSchemaStore.errorResponseSchema())
    record.put("code", code)
    record.put("message", message)
    record.put("errorMessage", description)
    record
  }

  def http(httpStatusCode: Int): HttpErrorResponse = {
    HttpErrorResponse(this.code, this.message, this.description, httpStatusCode)
  }
}

object HttpErrorResponse {
  def apply(errorResponse: ErrorResponse,
            httpStatusCode: Int): HttpErrorResponse  = {
    HttpErrorResponse(errorResponse.code,
      errorResponse.message,
      errorResponse.description,
      httpStatusCode)
  }
}
case class HttpErrorResponse(code: String,
                             message: String,
                             description: String = "",
                             httpStatusCode: Int) {
}


class DCSException(val errorResponse: ErrorResponse)
  extends Exception(errorResponse.code + ":" +
    errorResponse.message + ":" +
    errorResponse.description)

class HttpException(val errorResponse: HttpErrorResponse)
  extends Exception(errorResponse.code + ":" +
    errorResponse.message + ":" +
    errorResponse.description + ":" +
    errorResponse.httpStatusCode)
