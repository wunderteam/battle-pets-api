package com.wunder.pets.pets

import java.util.UUID
import javax.inject.Singleton

import com.wunder.pets.validations.Validations.ErrorMessages

import scala.concurrent.Future

@Singleton
class InMemoryPetRepository extends PetRepository {
  private val db = scala.collection.mutable.HashMap.empty[UUID, Pet]

  override def find(id: UUID): Future[Option[Pet]] = Future.successful(db.get(id))

  // This is a testing class so no need to worry over errors.
  @SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
  override def create(p: Pet): Future[Either[ErrorMessages, Pet]] = {
    db.put(p.id, p)

    Future.successful(Right(p))
  }

  override def list(): Future[List[Pet]] = Future.successful(List())
}
