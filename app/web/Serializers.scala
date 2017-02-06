package com.wunder.pets.web

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

import com.wunder.pets.pets.{CreatePetForm, Pet, Strength}
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

  implicit val petStrengthWrites = new Writes[Strength] {
    override def writes(o: Strength): JsValue = JsNumber(o.value)
  }

  implicit val petWrites = new Writes[Pet] {
    override def writes(o: Pet): JsValue = Json.obj(
      "id" -> Json.toJson(o.id),
      "strength" -> Json.toJson(o.strength.value)
    )
  }

  implicit val createPetFormReads: Reads[CreatePetForm] = (
    (JsPath \ "strength").read[Int] and
      (JsPath \ "name").read[String]
    ) (CreatePetForm.apply _)
}
