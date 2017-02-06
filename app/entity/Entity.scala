package com.wunder.pets.entity

import java.util.UUID

import com.wunder.pets.validations.Validations.WithValidationErrors

import scala.concurrent.Future

object Entity {
  type Create[DATA, ENTITY] = DATA => Future[WithValidationErrors[ENTITY]]
  type Find[ENTITY] = UUID => Future[Option[ENTITY]]
  type All[ENTITY] = () => Future[List[ENTITY]]
}
