package com.wunder.pets.web.controllers

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.wunder.pets.entity.Entity.{All, Create, Find}
import com.wunder.pets.pets.{CreatePetForm, Pet}
import com.wunder.pets.web.Serializers._
import play.api.libs.json._
import play.api.mvc.{Controller, _}
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.{Date,TimeZone}

import scala.concurrent.ExecutionContext

@Singleton
class PetsController @Inject()(findPet: Find[Pet], createPet: Create[CreatePetForm, Pet], allPets: All[Pet])
                              (implicit ec: ExecutionContext) extends Controller with CrudController {

  def all() = Action.async {
//    Future.successful(Ok("Pets"))
    allPets().map(pets => Ok(Json.toJson(pets)))
  }

  def find(id: UUID) = Action.async {
    findPet(id).map {
      case Some(pet) =>
        Ok(Json.toJson(pet))
      case None =>
        NotFound
    }
  }

  def create = Action.async(parse.json) { request =>
    createEntity(request, createPet)
  }

  def hello = Action {
    val nowAsISO = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )


    Ok(Json.toJson(Map("greeting" -> "HELLO WORLD!", "timestamp" -> nowAsISO)))
  }
}


