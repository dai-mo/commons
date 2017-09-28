package org.dcs.commons

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by cmathew on 11.11.16.
  */
object Control {

  def using[A <: { def close(): Unit }, B](param: A)(f: A => B): B =
    try {
      f(param)
    } finally {
      param.close()
    }

  def serialiseFutures[A, B](l: List[A])(fn: A => Future[B])
                            (implicit ec: ExecutionContext): Future[List[B]] =
    l.foldLeft(Future(List.empty[B])) {
      (previousFuture, next) =>
        for {
          previousResults <- previousFuture
          next <- fn(next)
        } yield previousResults :+ next
    }

}
