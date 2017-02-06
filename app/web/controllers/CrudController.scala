package com.wunder.pets.web.controllers

import com.wunder.pets.validations.Validations.{ErrorMessages, WithValidationErrors}
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.mvc.Results._
import play.api.mvc.{Request, Result}

import scala.concurrent.{ExecutionContext, Future}

trait CrudController {

  import com.wunder.pets.web.Serializers._

  protected def createEntity[DATA, ENTITY](request: Request[JsValue], create: DATA => Future[WithValidationErrors[ENTITY]])(implicit r: Reads[DATA], w: Writes[ENTITY], executionContext: ExecutionContext): Future[Result] = {
    val entityToResult = (validatedEntity: Future[WithValidationErrors[ENTITY]]) => {
      validatedEntity.map(_.fold(createFailure, createSuccess[ENTITY]))
    }

    request.body.validate[DATA].fold(invalidJson, create.andThen(entityToResult))
  }

  private def invalidJson(errors: Seq[(JsPath, Seq[ValidationError])]): Future[Result] =
    Future.successful(BadRequest(Json.obj("message" -> JsError.toJson(errors))))

  private def createSuccess[T](entity: T)(implicit w: Writes[T]): Result = Created(Json.toJson(entity))

  private def createFailure(messages: ErrorMessages): Result = UnprocessableEntity(Json.toJson(messages))
}
