package com.wunder.pets.pets

import java.util.UUID

import cats.implicits._
import com.wunder.pets.validations.{GreaterThan, NonEmptyString}
import com.wunder.pets.validations.Validations._

import scala.concurrent.{ExecutionContext, Future}


case class Pet(id: UUID, name: Name, strength: Strength, speed: Speed, intelligence: Intelligence, integrity: Integrity)


trait PetAttribute extends Any {
  val value: Int
}

case class Name private(value: String) extends AnyVal

object Name extends NonEmptyString {
  def validated(name: String) = validate("Pet name")(name).map(new Name(_))
}

case class Strength private(value: Int) extends AnyVal with PetAttribute

object Strength extends GreaterThan[Int] {
  def validated(s: Int) = validate(field = "Strength", lowerBound = 0)(s).map(new Strength(_))
}

case class Speed private(value: Int) extends AnyVal with PetAttribute

object Speed extends GreaterThan[Int] {
  def validated(s: Int) = validate(field = "Speed", lowerBound = 0)(s).map(new Speed(_))
}

case class Intelligence private(value: Int) extends AnyVal with PetAttribute

object Intelligence extends GreaterThan[Int] {
  def validated(s: Int) = validate(field = "Intelligence", lowerBound = 0)(s).map(new Intelligence(_))
}

case class Integrity private(value: Int) extends AnyVal with PetAttribute

object Integrity extends GreaterThan[Int] {
  def validated(s: Int) = validate(field = "Integrity", lowerBound = 0)(s).map(new Integrity(_))
}

case class CreatePetForm(name: String, strength: Int, speed: Int, intelligence: Int, integrity: Int)

object Pet {
  def create(petRepo: PetRepository)(formData: CreatePetForm)(implicit executionContext: ExecutionContext) = {
    val validatedPet = (
      Name.validated(formData.name)
        |@| Strength.validated(formData.strength)
        |@| Speed.validated(formData.speed)
        |@| Intelligence.validated(formData.intelligence)
        |@| Integrity.validated(formData.integrity)
      ).map(
      (name, strength, speed, intelligence, integrity) => Pet(
        id = UUID.randomUUID(),
        name = name,
        strength = strength,
        speed = speed,
        intelligence = intelligence,
        integrity = integrity
      )
    )

    def validPet(p: Pet) = petRepo.create(p)

    validatedPet.fold(handleValidationErrors, validPet)
  }

  def all(petRepo: PetRepository)() =
      petRepo.list()

  def find(petRepository: PetRepository)(id: UUID)(implicit executionContext: ExecutionContext): Future[Option[Pet]] =
    petRepository.find(id)
}


