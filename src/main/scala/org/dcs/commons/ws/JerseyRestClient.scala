package org.dcs.commons.ws


/**
  * Created by cmathew on 13.11.16.
  */

import javax.ws.rs.client.Invocation.Builder
import javax.ws.rs.client.{ClientBuilder, ClientRequestFilter, Entity}
import javax.ws.rs.core.{MediaType, Response}

import org.dcs.commons.error.{ErrorResponse, RESTException}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait JerseyRestClient extends ApiConfig {

  val client = ClientBuilder.newClient()

  def response(path: String,
               queryParams: List[(String, String)]  = List(),
               headers: List[(String, String)]= List()
              ): Builder = {
    var target = client.target(baseUrl)
    if(queryParams.nonEmpty)  queryParams.foreach(x => target = target.queryParam(x._1, x._2))

    var builder = target.path(path).request
    if(headers.nonEmpty)  headers.foreach(x => builder = builder.header(x._1, x._2))

    builder
  }

  def requestFilter(requestFilter: ClientRequestFilter): Unit = {
    client.register(requestFilter, 100)
  }

  private def responseOrError(response: Response): Either[ErrorResponse, Response] =
    if (response.getStatus >= 400 && response.getStatus < 600)
      Left(error(response.getStatus, response.readEntity(classOf[String])))
    else
      Right(response)


  private def responseOrException(response: Response): Response =
    if(response.getStatus >= 400 && response.getStatus < 600)
      throw new RESTException(error(response.getStatus, response.readEntity(classOf[String])))
    else
      response

  def getAsEither(path: String,
                  queryParams: List[(String, String)] = List(),
                  headers: List[(String, String)] = List()): Future[Either[ErrorResponse, Response]] =
    Future {
      val res: Response = response(path, queryParams, headers).get
      responseOrError(res)
    }

  def get(path: String,
          queryParams: List[(String, String)] = List(),
          headers: List[(String, String)] = List()): Future[Response] =
    Future {
      val res: Response = response(path, queryParams, headers).get
      responseOrException(res)
    }


  def getAsJson(path: String,
                queryParams: List[(String, String)] = List(),
                headers: List[(String, String)] = List()): Future[String] = {
    get(path, queryParams, headers).map(_.readEntity(classOf[String]))
  }

  def putAsEither[T](path: String,
                     body: T = AnyRef,
                     queryParams: List[(String, String)] = List(),
                     headers: List[(String, String)] = List(),
                     contentType: String = MediaType.APPLICATION_JSON): Future[Either[ErrorResponse, Response]] =
    Future {
      val res: Response = response(path, queryParams, headers).put(Entity.entity(body, contentType))
      responseOrError(res)
    }

  def put[T](path: String,
             body: T = AnyRef,
             queryParams: List[(String, String)] = List(),
             headers: List[(String, String)] = List(),
             contentType: String = MediaType.APPLICATION_JSON): Future[Response] =
    Future {
      val res: Response = response(path, queryParams, headers).put(Entity.entity(body, contentType))
      responseOrException(res)
    }

  def putAsJson[T](path: String,
                   body: T = AnyRef,
                   queryParams: List[(String, String)] = List(),
                   headers: List[(String, String)] = List(),
                   contentType: String = MediaType.APPLICATION_JSON): Future[String] = {
    put(path, body, queryParams, headers, contentType).map(_.readEntity(classOf[String]))
  }

  def postAsEither[T](path: String,
                      body: T = AnyRef,
                      queryParams: List[(String, String)] = List(),
                      headers: List[(String, String)] = List(),
                      contentType: String = MediaType.APPLICATION_JSON): Future[Either[ErrorResponse, Response]] =
    Future {
      val res: Response = response(path, queryParams, headers).post(Entity.entity(body, contentType))
      responseOrError(res)
    }

  def post[T](path: String,
              body: T = AnyRef,
              queryParams: List[(String, String)] = List(),
              headers: List[(String, String)] = List(),
              contentType: String = MediaType.APPLICATION_JSON): Future[Response] =
    Future {
      val res: Response = response(path, queryParams, headers).post(Entity.entity(body, contentType))
      responseOrException(res)
    }

  def postAsJson[T](path: String,
                    body: T = AnyRef,
                    queryParams: List[(String, String)] = List(),
                    headers: List[(String, String)] = List(),
                    contentType: String = MediaType.APPLICATION_JSON): Future[String] = {
    post(path, body, queryParams, headers, contentType).map(_.readEntity(classOf[String]))
  }

  def deleteAsEither(path: String,
                     queryParams: List[(String, String)] = List(),
                     headers: List[(String, String)] = List()): Future[Either[ErrorResponse, Response]] =
    Future {
      val res: Response = response(path, queryParams, headers).delete
      responseOrError(res)
    }

  def delete(path: String,
             queryParams: List[(String, String)] = List(),
             headers: List[(String, String)] = List()): Future[Response] =
    Future {
      val res: Response = response(path, queryParams, headers).delete
      responseOrException(res)
    }

  def deleteAsJson(path: String,
                   queryParams: List[(String, String)] = List(),
                   headers: List[(String, String)] = List()): Future[String] = {
    delete(path, queryParams, headers).map(_.readEntity(classOf[String]))
  }


}

