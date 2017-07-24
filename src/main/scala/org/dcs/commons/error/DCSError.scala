package org.dcs.commons.error

import org.apache.avro.generic.{GenericData, GenericRecord}
import org.dcs.commons.serde.AvroSchemaStore

import scala.beans.BeanProperty

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

  def validation(validationInfo: List[Map[String, String]]): ValidationErrorResponse = {
    ValidationErrorResponse(this.code, this.message, this.description, validationInfo)
  }

  def exception(): DCSException = {
    new DCSException(this)
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
                             httpStatusCode: Int)

object ValidationErrorResponse {
  val ErrorCode = "code"
  val ErrorMessage = "message"

  val ProcessorName = "processorName"
  val ProcessorId = "processorId"
  val ProcessorPropertyName = "processorPropertyName"
  val ProcessorPropertyType = "processorPropertyType"
  val ProcessorSchemaFieldName = "processorSchemaFieldName"
  val ProcessorSchemaFieldJsonPath = "processorSchemaFieldJsonPath"
  val ProcessorSchemaFieldType = "processorSchemaFieldType"

  def apply(errorResponse: ErrorResponse,
            validationInfo: List[Map[String, String]]): ValidationErrorResponse  = {
    ValidationErrorResponse(errorResponse.code,
      errorResponse.message,
      errorResponse.description,
      validationInfo)
  }

  def processorSchemaValidation(errorResponse: ErrorResponse,
                                processorName: String,
                                processorId: String): Map[String, String] = {
    Map(ErrorCode -> errorResponse.code,
      ErrorMessage -> errorResponse.message,
      ProcessorName -> processorName,
      ProcessorId -> processorId)
  }

  def processorSchemaFieldValidation(errorResponse: ErrorResponse,
                                     processorName: String,
                                     processorId: String,
                                     schemaPropertyName: String,
                                     schemaFieldName: String,
                                     jsonPath: String,
                                     fieldType: String): Map[String, String] = {
    Map(ErrorCode -> errorResponse.code,
      ErrorMessage -> errorResponse.message,
      ProcessorName -> processorName,
      ProcessorId -> processorId,
      ProcessorPropertyName -> schemaPropertyName,
      ProcessorSchemaFieldName -> schemaFieldName,
      ProcessorSchemaFieldJsonPath -> jsonPath,
      ProcessorSchemaFieldType -> fieldType
    )
  }

  def processorPropertyValidation(errorResponse: ErrorResponse,
                                  processorName: String,
                                  processorId: String,
                                  propertyName: String,
                                  propertyType: String): Map[String, String] = {
    Map(ErrorCode -> errorResponse.code,
      ErrorMessage -> errorResponse.message,
      ProcessorName -> processorName,
      ProcessorId -> processorId,
      ProcessorPropertyName -> propertyName,
      ProcessorPropertyType -> propertyType)
  }
}

case class ValidationErrorResponse(@BeanProperty var code: String,
                                   @BeanProperty var message: String,
                                   @BeanProperty var description: String = "",
                                   @BeanProperty var validationInfo: List[Map[String, String]])


//case class ProcessorSchemaValidation(processorName: String,
//                                     processorId: String)
//
//case class ProcessorSchemaFieldValidation(processorName: String,
//                                          processorId: String,
//                                          schemaPropertyName: String,
//                                          schemaFieldName: String,
//                                          jsonPath: String,
//                                          fieldType: String)
//
//case class ProcessorPropertyValidation(processorName: String,
//                                       processorId: String,
//                                       propertyName: String,
//                                       propertyType: String)

class DCSException(val errorResponse: ErrorResponse)
  extends Exception(errorResponse.code + ":" +
    errorResponse.message + ":" +
    errorResponse.description)

class HttpException(val errorResponse: HttpErrorResponse)
  extends Exception(errorResponse.code + ":" +
    errorResponse.message + ":" +
    errorResponse.description + ":" +
    errorResponse.httpStatusCode)

class ValidationException(val errorResponse: ValidationErrorResponse)
  extends Exception(errorResponse.code + ":" +
    errorResponse.message + ":" +
    errorResponse.description + ":" +
    errorResponse.validationInfo.toString)


