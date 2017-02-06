package com.wunder.pets.pets

import java.util.UUID

import cats.implicits._
import com.wunder.pets.validations.Validations._
import com.wunder.pets.validations._

import scala.concurrent.{ExecutionContext, Future}

case class Pet(id: UUID, strength: Strength, name: Name)

case class Name(value: String) extends AnyVal

object Name extends NonEmptyString {
  def validated(name: String) = validate("Pet name")(name).map(new Name(_))
}

case class Strength private(value: Int) extends AnyVal

object Strength extends GreaterThan[Int] {
  def validated(s: Int) = validate(field = "Strength", lowerBound = 0)(s).map(new Strength(_))
}

case class CreatePetForm(strength: Int, name: String)

object Pet {
  type FindPet = (UUID) => Future[Option[Pet]]
  type AllPets = () => Future[List[Pet]]

  def create(petRepo: PetRepository)(formData: CreatePetForm)(implicit executionContext: ExecutionContext) = {
    val validatedPet = (
      Name.validated(formData.name)
        |@| Strength.validated(formData.strength)
      ).map(
      (name, strength) => Pet(UUID.randomUUID(), strength, name)
    )

    def validPet(p: Pet) = petRepo.create(p)

    validatedPet.fold(handleValidationErrors, validPet)
  }

  def all(petRepo: PetRepository)(): Future[Seq[Pet]] = petRepo.list()

  def find(petRepository: PetRepository)(id: UUID)(implicit executionContext: ExecutionContext): Future[Option[Pet]] =
    petRepository.find(id)
}
