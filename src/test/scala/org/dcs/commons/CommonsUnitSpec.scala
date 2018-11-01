/*
 * Copyright (c) 2017-2018 brewlabs SAS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dcs.commons

import java.io.File

import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitSuite
import org.scalatest.mockito.MockitoSugar
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FlatSpec, _}



trait CommonsSpecUtil {

  def jsonFromFile(jsonFile: File): String = {
    val source = scala.io.Source.fromFile(jsonFile)
    try source.mkString finally source.close()
  }
}


trait CommonsBaseUnitSpec extends Matchers
  with OptionValues
  with Inside
  with Inspectors
  with MockitoSugar
  with CommonsSpecUtil
  with ScalaFutures {

  implicit val defaultPatience =
    PatienceConfig(timeout = Span(10, Seconds), interval = Span(100, Millis))

  // creates timeout in seconds for futures
  def timeout(secs: Int) =
    Timeout(Span(secs, Seconds))

}

abstract class CommonsUnitSpec  extends FlatSpec
  with CommonsBaseUnitSpec
  with BeforeAndAfterEach
  with BeforeAndAfter
  with BeforeAndAfterAll

abstract class AsyncCommonsUnitSpec extends AsyncFlatSpec
  with CommonsBaseUnitSpec
  with BeforeAndAfterEach
  with BeforeAndAfter
  with BeforeAndAfterAll

// FIXME: Currently the only way to use the mockito
// inject mock mechanism to test the CDI
// part is to run the test as JUnit tests
// since there is no mechanism to run this
// as a scala test.
// ScalaMock could be an option once the
// issue https://github.com/paulbutcher/ScalaMock/issues/100
// is resolved
abstract class JUnitSpec extends JUnitSuite
  with Matchers
  with OptionValues
  with Inside
  with Inspectors
  with MockitoSugar
  with CommonsSpecUtil

object IT extends Tag("IT")

