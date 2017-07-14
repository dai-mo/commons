package org.dcs.commons.ws

import org.dcs.commons.error.HttpErrorResponse


trait ApiConfig {
  
  def baseUrl():String

  def error(status: Int, message: String): HttpErrorResponse

  def endpoint(path: String): String = {
    baseUrl() + path
  }
  
}