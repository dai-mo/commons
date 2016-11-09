package org.dcs.commons.ws

import org.dcs.commons.error.ErrorResponse


trait ApiConfig {
  
  def baseUrl():String

  def error(status: Int, message: String): ErrorResponse

  def endpoint(path: String): String = {
    baseUrl() + path
  }
  
}