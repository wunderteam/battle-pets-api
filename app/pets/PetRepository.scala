package com.wunder.pets.pets

import java.util.UUID

import com.wunder.pets.validations.Validations.ErrorMessages

import scala.concurrent.Future

trait PetRepository {
  def find(id: UUID): Future[Option[Pet]]

//  def list(): Future[List[Pet]]

  def create(p: Pet): Future[Either[ErrorMessages, Pet]]
}
