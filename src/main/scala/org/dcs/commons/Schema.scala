package org.dcs.commons

import org.apache.avro.Schema
import org.apache.avro.Schema.Type
import org.dcs.commons.serde.JsonSerializerImplicits._

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
//  def apply(schemaField: Schema.Field):SchemaField = {
//    new SchemaField(schemaField.name(),
//      schemaField.schema().getType.getName,
//      schemaField.doc(),
//      schemaField.defaultVal())
//  }

  def validatePath(schema: Schema, schemaPath: String): Boolean = {

    def validatePath(currentSchema: Schema, path:List[String]): Boolean = path match {
      case Nil  => if(currentSchema.getType != Schema.Type.RECORD) true else false
      case _ if currentSchema.getType != Schema.Type.RECORD  => false
      case _ => {
        val field = currentSchema.getField(path.head)
        if(field  == null)
          false
        else
          validatePath(field.schema(), path.tail)
      }
    }

    validatePath(schema, schemaPath.split("\\.").toList.tail)
  }
}
case class SchemaField(@BeanProperty var name: String,
                       @BeanProperty var `type`: AnyRef,
                       @BeanProperty var doc: String,
                       @BeanProperty var defaultValue: AnyRef) {
  def this() = this("", null, "", null)

  def toAvroField: Schema.Field = {

    val schema: Schema =
      new Schema.Parser().parse(`type`.toJson)

    new Schema.Field(name,
      schema,
      doc,
      defaultValue)
  }
}

case class SchemaType(@BeanProperty var name: String,
                      @BeanProperty var `type`: String,
                      @BeanProperty var fields: List[SchemaField]) {
  def this() = this("", "", Nil)
}

case class SchemaAction(@BeanProperty var action: String,
                        @BeanProperty var avroPath: String,
                        @BeanProperty var field: SchemaField = null) {
  def this() = this("", "", null)
}
