package org.dcs.commons.error

/**
  * Factory for all known error responses, divided into
  * relevant categories in blocks of 100 each.
  *
  * Note: The first error for each category (i.e. 100, 200, etc)
  * is reserved and should be used to representing 'any' error
  * for the corresponding category.
  *
  * Created by cmathew on 05/06/16.
  */
object ErrorConstants {
  val GlobalGeneralErrorCode = "DCS000"
  val GlobalDataErrorCode = "DCS100"
  val GlobalServiceErrorCode = "DCS200"
  val GlobalFlowErrorCode = "DCS300"
  val GlobalClientErrorCode = "DCS400"
  val GlobalDSecurityErrorCode = "DCS500"

  val UnknownErrorResponse = ErrorResponse("DCS001", "Unknown error")

  /*
    General errors
   */
  val DCS001 = UnknownErrorResponse
  val DCS002 =  ErrorResponse("DCS002", "Unexpected Error")
  val DCS003 =  ErrorResponse("DCS003", "Request is not authorised")

  /*
    Data related errors
   */
  val DCS101 = ErrorResponse("DCS101", "Datasource with given name already exists")
  val DCS102 = ErrorResponse("DCS102", "Error loading data")
  val DCS103 = ErrorResponse("DCS103", "Error initializing data store")
  val DCS104 = ErrorResponse("DCS104", "Error reading data")
  val DCS105 = ErrorResponse("DCS105", "Error writing data")
  val DCS106 = ErrorResponse("DCS106", "Error initialising data admin")
  val DCS107 = ErrorResponse("DCS107", "Error loading / retrieving data source info")
  val DCS108 = ErrorResponse("DCS108", "Requested entity is not available")

  /*
    Service related errors
   */
  val DCS201 = ErrorResponse("DCS201", "Service currently unavailable")

  /*
    Flow related errors
   */
  val DCS301 = ErrorResponse("DCS301", "Requested entity is not available")
  val DCS302 = ErrorResponse("DCS302", "Access to flow entity is unauthorized")
  val DCS303 = ErrorResponse("DCS303", "Access to flow entity is forbidden")
  val DCS304 = ErrorResponse("DCS304", "Flow entity could not be found")
  val DCS305 = ErrorResponse("DCS305", "Invalid request to process flow entity")
  val DCS306 = ErrorResponse("DCS306", "Processor Validation Error")
  val DCS307 = ErrorResponse("DCS307", "Missing Processor Input Schema")
  val DCS308 = ErrorResponse("DCS308", "Missing Processor Output Schema")
  val DCS309 = ErrorResponse("DCS309", "Missing Processor Schema Field")
  val DCS310 = ErrorResponse("DCS310", "Invalid Processor Schema Field Path")
  val DCS311 = ErrorResponse("DCS311", "Invalid Processor Schema Field Type")
  val DCS312 = ErrorResponse("DCS312", "Empty Processor Schema Property")
  val DCS313 = ErrorResponse("DCS313", "Missing Required Processor Property")
  val DCS314 = ErrorResponse("DCS314", "Empty Required Processor Property")
  val DCS315 = ErrorResponse("DCS315", "Invalid Connection")
  val DCS316 = ErrorResponse("DCS316", "Missing Entity Information")

  /*
    Web client errors
   */
  val DCS400 = ErrorResponse("DCS400", "Request is malformed")

  /*
  Security errors
 */
  val DCS500 = ErrorResponse("DCS500", "Unkown authorisation error")
  val DCS501 = ErrorResponse("DCS501", "Access token is missing")
  val DCS502 = ErrorResponse("DCS502", "Access token is invalid")
  val DCS503 = ErrorResponse("DCS503", "Requested action is no authorised")

}


