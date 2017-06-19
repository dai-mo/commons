package org.dcs.commons

import org.apache.avro.Schema
import org.apache.avro.Schema.Type

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

/**
  * Created by cmathew on 01.06.17.
  */
object SchemaAction {
  val SCHEMA_ADD_ACTION = "add"
  val SCHEMA_REM_ACTION = "rem"

  val Primitives: Map[String, Schema.Type] =
    Map("string" -> Type.STRING,
      "bytes" -> Type.BYTES,
      "int" -> Type.INT,
      "long" -> Type.LONG,
      "float" -> Type.FLOAT,
      "double" -> Type.DOUBLE,
      "boolean" -> Type.BOOLEAN,
      "null" -> Type.NULL)

}

object SchemaField {
  def apply(schemaField: Schema.Field):SchemaField = {
    new SchemaField(schemaField.name(),
      schemaField.schema().getType.getName,
      schemaField.doc(),
      schemaField.defaultVal())
  }
}
case class SchemaField(@BeanProperty var name: String,
                       @BeanProperty var schemaType: String,
                       @BeanProperty var doc: String,
                       @BeanProperty var defaultValue: Object) {
  def this() = this("", "", "", null)
  def toAvroField = new Schema.Field(name,
    Schema.createUnion(List(Schema.create(Schema.Type.NULL),Schema.create(SchemaAction.Primitives(schemaType))).asJava),
    doc,
    defaultValue)
}

case class SchemaAction(@BeanProperty var action: String,
                        @BeanProperty var avroPath: String,
                        @BeanProperty var field: SchemaField = null) {
  def this() = this("", "", null)
}
