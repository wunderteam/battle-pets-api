package com.wunder.pets.entity

import java.util.UUID

import com.wunder.pets.validations.Validations.WithErrorMessages

import scala.concurrent.Future

object Entity {
  type Create[DATA, ENTITY] = DATA => Future[WithErrorMessages[ENTITY]]
  type Find[ENTITY] = UUID => Future[Option[ENTITY]]
  type All[ENTITY] = () => Future[Seq[ENTITY]]
}
