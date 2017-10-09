package org.dcs.commons.ws

import java.io.IOException
import javax.ws.rs.client.{ClientRequestContext, ClientRequestFilter}

import org.slf4j.LoggerFactory

object DetailedLoggingFilter {
  private val LOG = LoggerFactory.getLogger(classOf[DetailedLoggingFilter].getName)
}

class DetailedLoggingFilter extends ClientRequestFilter {
  import DetailedLoggingFilter._

  @throws[IOException]
  override def filter(requestContext: ClientRequestContext): Unit = {
    if(requestContext != null && requestContext.getEntity != null)
      LOG.info(requestContext.getEntity.toString)
  }
}
