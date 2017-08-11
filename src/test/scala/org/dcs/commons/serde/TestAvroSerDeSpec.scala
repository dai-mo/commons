package org.dcs.commons.serde

import java.io.File

import org.apache.commons.io.IOUtils
import org.dcs.commons.{CommonsUnitSpec, SchemaAction, SchemaField}
import org.dcs.data.model.{User, UserWithAge}

import scala.collection.mutable
import AvroImplicits._
import org.apache.avro.{AvroRuntimeException, Schema}
import org.apache.avro.data.Json
import org.apache.avro.generic.{GenericData, GenericRecord}
import JsonSerializerImplicits._
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

    val rec = json.toGenericRecord(Some(schemaForUser))
    assert(rec.get("first_name").toString == firstName)
    assert(rec.get("middle_name").toString == middleName)
    assert(rec.get("last_name").toString == lastName)
    assert(rec.get("age") == null)

    val schemaForUserNoMName: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user.avsc"))

    val jsonNoMName = IOUtils.toString(this.getClass.getResourceAsStream("/user-without-mname.json"), "UTF-8")

    val recNoName = jsonNoMName.toGenericRecord(Some(schemaForUserNoMName))
    assert(recNoName.get("first_name").toString == firstName)
    assert(recNoName.get("middle_name") == null)
    assert(recNoName.get("last_name").toString == lastName)
    assert(recNoName.get("age") == null)
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

  "Avro Schema Update" should "work when deleting field" in {
    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user-with-address.avsc"))

    val MNameFieldName = "middle_name"
    val AddressFieldName = "address"
    val CityFieldName = "city"

    val remMNameAction = SchemaAction(SchemaAction.SCHEMA_REM_ACTION,
      JsonPath.Root + JsonPath.Sep + MNameFieldName)

    val upSchema1 = schemaForUser.update(List(remMNameAction))
    assert(Option(upSchema1.getField(MNameFieldName)).isEmpty)
    assert(upSchema1.getFields.size() == 3)
    assert(Option(upSchema1.getField(AddressFieldName).schema().getField(CityFieldName)).isDefined)

    val remCityAction = SchemaAction(SchemaAction.SCHEMA_REM_ACTION,
      JsonPath.Root + JsonPath.Sep + AddressFieldName + JsonPath.Sep + CityFieldName)

    val upSchema2 = schemaForUser.update(List(remCityAction))
    assert(Option(upSchema2.getField(AddressFieldName).schema().getField(CityFieldName)).isEmpty)

    val remAddressAction = SchemaAction(SchemaAction.SCHEMA_REM_ACTION,
      JsonPath.Root + JsonPath.Sep + AddressFieldName)

    val upSchema3 = schemaForUser.update(List(remAddressAction))
    assert(Option(upSchema3.getField(AddressFieldName)).isEmpty)

    upSchema3.update(List(remAddressAction))
  }

  "Avro Schema Update" should "work when adding field" in {
    val schemaForUser: Schema = new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user.avsc"))

    val TitleFieldName = "title"
    val AddressFieldName = "address"
    val CityFieldName = "city"
    val PinCodeName = "pincode"

    val addTitleAction1 = SchemaAction(SchemaAction.SCHEMA_ADD_ACTION,
      JsonPath.Root,
      SchemaField(TitleFieldName, Schema.Type.STRING.getName, "", ""))

    var upSchema = schemaForUser.update(List(addTitleAction1))

    assert(upSchema.getFields.size() == 4)
    assert(Option(upSchema.getField(TitleFieldName)).isDefined)

    val addAddressAction =
      this.getClass.getResourceAsStream("/avro-gen/addAddressAction.json").toObject[SchemaAction]

    val upSchemaWithAddress = upSchema.update(List(addAddressAction))

    assert(Option(upSchemaWithAddress.getField(AddressFieldName).schema().getField(CityFieldName)).isDefined)

    val addPinCodeAction = SchemaAction(SchemaAction.SCHEMA_ADD_ACTION,
      JsonPath.Root + JsonPath.Sep + AddressFieldName,
      SchemaField(PinCodeName, Schema.Type.STRING.getName, "", ""))

    val addTitleAction2 = SchemaAction(SchemaAction.SCHEMA_ADD_ACTION,
      JsonPath.Root,
      SchemaField(TitleFieldName, Schema.Type.STRING.getName, "", ""))

    val upSchema2 = upSchemaWithAddress.update(List(addTitleAction2, addPinCodeAction))

    assert(upSchema2.getFields.size() == 5)
    assert(Option(upSchema2.getField(TitleFieldName)).isDefined)

    assert(upSchema2.getField(AddressFieldName).schema().getFields.size() == 3)
    assert(Option(upSchema2.getField(AddressFieldName).schema().getField(PinCodeName)).isDefined)

    upSchema2.update(List(addTitleAction2, addPinCodeAction))

  }

  "Avro Schema Update" should "work when combining addition and deletion of field" in {

    val schemaForUser: Schema =
      new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user-with-address.avsc"))


    val MNameFieldName = "middle_name"
    val AddressFieldName = "address"
    val CityFieldName = "city"
    val PinCodeName = "pincode"

    val remMNameAction = this.getClass.getResourceAsStream("/avro-gen/remMNameAction.json").toObject[SchemaAction]

    val addPinCodeAction = this.getClass.getResourceAsStream("/avro-gen/addPincodeAction.json").toObject[SchemaAction]

    val upSchema = schemaForUser.update(List(remMNameAction, addPinCodeAction))

    assert(Option(upSchema.getField(MNameFieldName)).isEmpty)
    assert(upSchema.getFields.size() == 3)
    assert(Option(upSchema.getField(AddressFieldName).schema().getField(CityFieldName)).isDefined)

    assert(upSchema.getField(AddressFieldName).schema().getFields.size() == 3)
    assert(Option(upSchema.getField(AddressFieldName).schema().getField(PinCodeName)).isDefined)

  }

  "Avro Schema Path Validation" should "be consistent" in {

    val schemaForUser: Schema =
      new Schema.Parser().parse(this.getClass.getResourceAsStream("/avro-gen/user-with-address.avsc"))


    val MNameFieldName = "middle_name"
    val AddressFieldName = "address"
    val CityFieldName = "city"
    val PinCodeName = "pincode"

    assert(SchemaField.validatePath(schemaForUser, JsonPath.Root + JsonPath.Sep + MNameFieldName))
    assert(!SchemaField.validatePath(schemaForUser, JsonPath.Root + JsonPath.Sep + MNameFieldName + JsonPath.Sep + "initial"))

    assert(SchemaField.validatePath(schemaForUser, JsonPath.Root + JsonPath.Sep + AddressFieldName + JsonPath.Sep + CityFieldName))
    assert(!SchemaField.validatePath(schemaForUser, JsonPath.Root + JsonPath.Sep + AddressFieldName + JsonPath.Sep + PinCodeName))

    assert(!SchemaField.validatePath(schemaForUser, JsonPath.Root + JsonPath.Sep))
    assert(!SchemaField.validatePath(schemaForUser, ""))

  }

}
