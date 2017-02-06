package com.wunder.pets.web

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

import com.wunder.pets.pets._
import com.wunder.pets.validations.Validations.ErrorMessages
import play.api.libs.functional.syntax._
import play.api.libs.json._

object Serializers {
  // Order is important here (compiler needs to know about any implicit Reads/Writes before it uses them):

  implicit val uuidWrites = new Writes[UUID] {
    override def writes(o: UUID): JsValue = JsString(o.toString)
  }

  implicit val offsetDateTimeWrites = new Writes[OffsetDateTime] {
    override def writes(o: OffsetDateTime): JsValue = JsString(o.format(DateTimeFormatter.ISO_INSTANT))
  }

  implicit val errorMessagesWrites = new Writes[ErrorMessages] {
    override def writes(o: ErrorMessages): JsValue = Json.obj(
      "errors" -> o.map(JsString(_))
    )
  }

  def newAttributeWriter[T <: PetAttribute]: Writes[T] = {
    new Writes[T] {
      override def writes(o: T): JsValue = JsNumber(o.value)
    }
  }

  implicit val petStrengthWrites = newAttributeWriter[Strength]
  implicit val petSpeedWrites = newAttributeWriter[Speed]
  implicit val petIntelligenceWrites = newAttributeWriter[Intelligence]
  implicit val petIntegrityWrites = newAttributeWriter[Integrity]
  implicit val petNameWrites = new Writes[Name] {
    override def writes(o: Name): JsValue = JsString(o.value)
  }

  implicit val petWrites = new Writes[Pet] {
    override def writes(o: Pet): JsValue = Json.obj(
      "id" -> Json.toJson(o.id),
      "name" -> Json.toJson(o.name),
      "strength" -> Json.toJson(o.strength),
      "speed" -> Json.toJson(o.speed),
      "intelligence" -> Json.toJson(o.intelligence),
      "integrity" -> Json.toJson(o.integrity)
    )
  }

  implicit val createPetFormReads: Reads[CreatePetForm] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "strength").read[Int] and
      (JsPath \ "speed").read[Int] and
      (JsPath \ "intelligence").read[Int] and
      (JsPath \ "integrity").read[Int]
    ) (CreatePetForm.apply _)
}
