package org.dcs.commons

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

}
