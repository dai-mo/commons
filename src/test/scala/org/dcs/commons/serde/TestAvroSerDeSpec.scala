package org.dcs.commons.serde

import java.io.File

import org.apache.commons.io.IOUtils
import org.dcs.commons.CommonsUnitSpec
import org.dcs.data.model.{User, UserWithAge}

import scala.collection.mutable
import AvroImplicits._
import org.apache.avro.Schema
import org.apache.avro.data.Json
import org.apache.avro.generic.{GenericData, GenericRecord}
/**
  * Created by cmathew on 24.10.16.
  */
class TestAvroSerDeSpec extends CommonsUnitSpec {

  val firstName = "Obi"
  val middleName = "Wan"
  val lastName = "Kenobi"
  val age = 1024
  val defaultAge = -1


  "Specific Avro Ser De" should "be valid for same reader writer schema when writing to file" in {



    val user: User = new User()
    user.setFirstName(firstName)
    user.setMiddleName(middleName)
    user.setLastName(lastName)


    val file = File.createTempFile("user", ".avro")

    List(user).serToFile(Some(user.getSchema), file)

    val data = file.deSerToObjects(None)

    assert(user.getFirstName.toString == firstName)
    assert(user.getMiddleName.toString == middleName)
    assert(user.getLastName.toString == lastName)


  }

  "Specific Avro Ser De" should "be valid for same reader writer schema when writing to bytes" in {


    val user: User = new User()
    user.setFirstName(firstName)
    user.setMiddleName(middleName)
    user.setLastName(lastName)


    val bytes = user.serToBytes(Some(user.getSchema))

    val str = new String(bytes)
    val schema = Some(user.getSchema)
    val userDeSer:User = bytes.deSerToObject(schema, schema)

    assert(userDeSer.getFirstName.toString == firstName)
    assert(userDeSer.getMiddleName.toString == middleName)
    assert(userDeSer.getLastName.toString == lastName)


  }

  "Specific Avro Ser De" should "be valid for missing reader schema field" in {

    // verifies schema resolution for the scenario that ....
    // if the writer's record contains a field with a name not present
    // in the reader's record, the writer's value for that field is ignored.


    val user = new UserWithAge()
    user.setFirstName(firstName)
    user.setMiddleName(middleName)
    user.setLastName(lastName)
    user.setAge(age)

    val bytes = user.serToBytes(Some(user.getSchema))

    val schema = Some(User.getClassSchema)
    val userWithoutAge: User = bytes.deSerToObject(Some(user.getSchema), schema)

    assert(userWithoutAge.getFirstName.toString == firstName)
    assert(userWithoutAge.getMiddleName.toString == middleName)
    assert(userWithoutAge.getLastName.toString == lastName)

  }

  "Generic Avro Ser De" should "be valid for new field in reader schema" in {

    // verifies schema resolution for the scenario that ....
    // if the reader's record schema has a field that contains a default value,
    // and writer's schema does not have a field with the same name,
    // then the reader should use the default value from its field.

    def assertUser(data: GenericRecord): Unit = {

      assert(data.get("first_name").toString == firstName)
      assert(data.get("middle_name").toString == middleName)
      assert(data.get("last_name").toString == lastName)
      assert(data.get("age").asInstanceOf[Int] == defaultAge)
    }

    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user.avsc"))

    val user = new GenericData.Record(schemaForUser)
    user.put("first_name", firstName)
    user.put("middle_name", middleName)
    user.put("last_name", lastName)

    val bytes = user.serToBytes(Some(schemaForUser))

    val schemaForUserWithAge: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user-with-age.avsc"))

    val data: GenericRecord = bytes.deSerToGenericRecord(Some(schemaForUser), Some(schemaForUserWithAge))

    assertUser(data)
  }

  "Generic Avro Ser De" should "not work for new field in reader schema without default value" in {

    // verifies schema resolution for the scenario that ....
    // if the reader's record schema has a field with no default value,
    // and writer's schema does not have a field with the same name, an error is signalled.


    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user.avsc"))


    val user = new GenericData.Record(schemaForUser)
    user.put("first_name", firstName)
    user.put("middle_name", middleName)
    user.put("last_name", lastName)

    val bytes: Array[Byte] = user.serToBytes(Some(schemaForUser))

    val schemaForUserWithAge: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user-with-age.avsc"))

    val data = bytes.deSerToGenericRecord(Some(schemaForUser), Some(schemaForUserWithAge))

  }

  "Generic Avro Ser De" should "be valid for missing filed in reader schema" in {

    // verifies schema resolution for the scenario that ....
    // if the writer's record contains a field with a name not present
    // in the reader's record, the writer's value for that field is ignored.

    def assertUser(data: GenericRecord): Unit = {

      assert(data.get("first_name").toString == firstName)
      assert(data.get("middle_name").toString == middleName)
      assert(data.get("last_name").toString == lastName)
      assert(data.get("age") == null)
    }

    val schemaForUserWithAge: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user-with-age.avsc"))

    val user = new GenericData.Record(schemaForUserWithAge)
    user.put("first_name", firstName)
    user.put("middle_name", middleName)
    user.put("last_name", lastName)
    user.put("age", age)

    val bytes = user.serToBytes(Some(schemaForUserWithAge))

    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user.avsc"))

    val data = bytes.deSerToGenericRecord(Some(schemaForUserWithAge), Some(schemaForUser))

    assertUser(data)
  }

  "Generic Json Avro Ser De" should "be valid for missing filed in writer schema" in {

    // verifies schema resolution for the scenario that ....
    // if the writer's record contains a field with a name not present
    // in the reader's record, the writer's value for that field is ignored.

    def assertUser(data: GenericRecord): Unit = {

      assert(data.get("first_name").toString == firstName)
      assert(data.get("middle_name").toString == middleName)
      assert(data.get("last_name").toString == lastName)
      assert(data.get("age") == null)
    }

    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user.avsc"))

    val json = IOUtils.toString(this.getClass.getResourceAsStream("/user.json"), "UTF-8")

    val bytes = json.serToBytes(Some(schemaForUser))

    val data = bytes.deSerToJsonString(Some(schemaForUser), Some(schemaForUser))

    assertUser(data.toGenericRecord(Some(schemaForUser)))
  }

  "Arbitrary Json Avro Ser De" should "be valid for aribtrary json" in {

    def assertUser(data: mutable.Map[String, AnyRef]): Unit = {
      assert(data.size == 4)
      assert(data("first_name") == firstName)
      assert(data("middle_name") == middleName)
      assert(data("last_name") == lastName)
      assert(data("age").asInstanceOf[Long] == age)
    }



    val json = IOUtils.toString(this.getClass.getResourceAsStream("/user.json"), "UTF-8")

    val jsonObj = Json.parseJson(json)
    val bytes = jsonObj.serToBytes()

    val data = bytes.deSerToJsonMap()

    assertUser(data)
  }

}