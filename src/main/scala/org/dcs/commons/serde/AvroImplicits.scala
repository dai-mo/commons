package org.dcs.commons.serde

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, File}

import org.apache.avro.Schema
import org.apache.avro.data.Json
import org.apache.avro.file.{DataFileReader, DataFileWriter}
import org.apache.avro.generic.{GenericDatumReader, GenericDatumWriter, GenericRecord}
import org.apache.avro.io._
import org.apache.avro.specific.{SpecificDatumReader, SpecificDatumWriter}
import org.dcs.commons.Control._
import org.dcs.commons.SchemaAction

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Created by cmathew on 11.11.16.
  */
object AvroImplicits {

  // ------ Serlializers - Start --------

  implicit class ObjectListSer(objs: List[AnyRef]) {
    def serToFile[T](schema: Option[Schema], file: File) = {
      val datumWriter: DatumWriter[T] = new SpecificDatumWriter[T]
      val dataFileWriter: DataFileWriter[T] = new DataFileWriter[T](datumWriter)
      dataFileWriter.create(schema.get, file)
      objs.asInstanceOf[List[T]].foreach(dso => dataFileWriter.append(dso))
      dataFileWriter.close()
    }
  }

  implicit class GenericRecordListSer(records: List[GenericRecord]) {
    def serToFile(schema: Option[Schema], file: File) = {
      val datumWriter: DatumWriter[GenericRecord] = new GenericDatumWriter[GenericRecord](schema.get)
      val dataFileWriter: DataFileWriter[GenericRecord] = new DataFileWriter[GenericRecord](datumWriter)
      dataFileWriter.create(schema.get, file)
      records.foreach(dso => dataFileWriter.append(dso))
      dataFileWriter.close()
    }
  }

  implicit class ObjectSer[T](obj: T) {

    private def jsonToBytes(jsonObj: AnyRef): Array[Byte] = {
      using(new ByteArrayOutputStream()) { out =>
        val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
        val writer: Json.ObjectWriter = new Json.ObjectWriter()
        writer.write(jsonObj, encoder)
        encoder.flush()
        out.toByteArray
      }
    }

    def serToBytes(schema: Option[Schema] = None): Array[Byte] = obj match {
      case jsonString: String =>
        if(schema.isDefined)
          jsonString.toGenericRecord(schema).serToBytes(schema)
        else
          jsonToBytes(Json.parseJson(jsonString))
      case obj: AnyRef if schema.isEmpty => jsonToBytes(obj)
      case record: GenericRecord =>
        using(new ByteArrayOutputStream()) { out =>
          val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
          val writer: DatumWriter[GenericRecord] = new GenericDatumWriter[GenericRecord](schema.get)
          writer.write(record, encoder)
          encoder.flush()
          out.toByteArray
        }
      case _ =>
        using(new ByteArrayOutputStream()) { out =>
          val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
          val writer: DatumWriter[T] = new SpecificDatumWriter[T](schema.get)

          writer.write(obj, encoder)
          encoder.flush()
          out.toByteArray
        }
    }
  }

  // ------ Serlializers - End ---------

  // ------ DeSerlializers - Start -----

  implicit class FileDeSer(file: File) {
    def genData[T](dfr: DataFileReader[T]): List[T] = dfr.hasNext match {
      case true => dfr.next :: genData(dfr)
      case false => Nil
    }

    def deSerToObjects[T](schema: Option[Schema]): List[T] = {
      val rSchema = if (schema.isDefined) schema.get else null
      val datumReader: DatumReader[T] = new SpecificDatumReader[T](null, rSchema)
      val dataFileReader: DataFileReader[T] = new DataFileReader[T](file, datumReader);
      genData(dataFileReader)
    }

    def deSerToGenericRecords(schema: Option[Schema], file: File): List[GenericRecord] = {
      val datumReader: DatumReader[GenericRecord] = new GenericDatumReader[GenericRecord](schema.get);
      val dataFileReader: DataFileReader[GenericRecord] = new DataFileReader[GenericRecord](file, datumReader);
      genData(dataFileReader)
    }
  }

  implicit class ByteArrayDeSer(bytes: Array[Byte]) {

    def deSerToObject[T](wSchema: Option[Schema], rSchema: Option[Schema]): T = {
      val reader: SpecificDatumReader[T] = new SpecificDatumReader[T](wSchema.get, rSchema.get)
      val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, null)
      reader.read(null.asInstanceOf[T], decoder)
    }

    def deSerToGenericRecord(wSchema: Option[Schema], rSchema: Option[Schema]): GenericRecord = {
      val reader = new GenericDatumReader[GenericRecord](wSchema.get, rSchema.get)
      val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, null)
      reader.read(null.asInstanceOf[GenericRecord], decoder)
    }

    def deSerToJsonString(wSchema: Option[Schema], rSchema: Option[Schema]): String = {
      val record: GenericRecord = deSerToGenericRecord(wSchema, rSchema)
      using(new ByteArrayOutputStream()) { out =>
        val writer: DatumWriter[GenericRecord] = new GenericDatumWriter[GenericRecord](rSchema.get)
        val encoder: Encoder = EncoderFactory.get().jsonEncoder(rSchema.get, out, true)
        writer.write(record, encoder)

        encoder.flush()
        new String(out.toByteArray)
      }
    }

    def deSerToJsonMap(): mutable.Map[String, AnyRef] = {
      val reader: Json.ObjectReader = new Json.ObjectReader
      val decoder: Decoder = DecoderFactory.get().binaryDecoder(bytes, null)
      reader.read(null.asInstanceOf[AnyRef], decoder).asInstanceOf[java.util.LinkedHashMap[String, AnyRef]].asScala
    }

    def deSerToGenericRecord(): GenericRecord = {
      deSerToGenericRecord(Some(Json.SCHEMA), Some(Json.SCHEMA))
    }
  }
  // ------ DeSerlializers - End --------

  // ------ Converters - Start ----------

  implicit class JsonStringSer(jsonStr: String) {
    def toGenericRecord(schema: Option[Schema]): GenericRecord = {
      using(new ByteArrayInputStream(jsonStr.getBytes())) { in =>
        val reader: DatumReader[GenericRecord] = new GenericDatumReader[GenericRecord](schema.get)
        val decoder: Decoder = DecoderFactory.get().jsonDecoder(schema.get, in)
        reader.read(null, decoder)
      }
    }
  }

  // ------ Converters - End ------------

  // ------ Schema Utils - End ------------

  implicit class ExtendSchemaField(field: Schema.Field) {
    def copy: Schema.Field = {
      val schemaClone = new Schema.Field(field.name(), field.schema().copy, field.doc(), field.defaultVal())
      field.aliases().asScala.foreach(a => schemaClone.addAlias(a))
      schemaClone
    }
  }

  implicit class ExtendSchema(schema: Schema) {
    def copy: Schema =
      if(schema.getType == Schema.Type.RECORD)
        Schema.createRecord(schema.getName, schema.getDoc, schema.getNamespace, false)
      else
        schema
  }

  implicit class SchemaUpdate(schema: Schema) {

    def update(actions: List[SchemaAction]): Schema = {
      // Currently we only update schemas of type RECORD
      if(schema.getType != Schema.Type.RECORD) return schema

      val updatedSchema: Schema = Schema.createRecord(schema.getName, schema.getDoc, schema.getNamespace, false)
      update(actions, schema, updatedSchema, JsonPath.Root)
      updatedSchema
    }

    def update(actions: List[SchemaAction],
               currentSchema: Schema,
               updatedSchema: Schema,
               currentAvroPath: String): Unit = {

      def fieldAvroPath(fieldName: String): String = currentAvroPath + "." + fieldName

      if(currentSchema.getType != Schema.Type.RECORD) return

      val fields = currentSchema.getFields.asScala.toList

      val actionPartitions =
        actions.
          partition(a => fields.exists(sf => a.action match {
            case SchemaAction.SCHEMA_ADD_ACTION => a.avroPath == currentAvroPath
            case SchemaAction.SCHEMA_REM_ACTION => a.avroPath == fieldAvroPath(sf.name())
            case _ => false
          }))

      val fieldsAfterRemoval = fields.filterNot(sf =>
        actions.exists(a =>
          a.avroPath == fieldAvroPath(sf.name()) &&
            a.action == SchemaAction.SCHEMA_REM_ACTION))

      val fieldCopiesAfterRemoval = fieldsAfterRemoval.
        map(_.copy)


      val fieldsAfterAddition: List[Schema.Field] =
        fieldCopiesAfterRemoval ::: actions.filter(a =>
          a.avroPath == currentAvroPath &&
            a.action == SchemaAction.SCHEMA_ADD_ACTION).map(_.field.toAvroField)

      updatedSchema.setFields(fieldsAfterAddition.asJava)

      fieldsAfterRemoval.zip(fieldCopiesAfterRemoval).foreach(sf =>
        update(actionPartitions._2,
          sf._1.schema(),
          sf._2.schema(),
          fieldAvroPath(sf._1.name())))
    }
  }

  // ------ Schema Utils - End ------------
}

