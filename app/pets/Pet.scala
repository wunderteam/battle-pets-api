package com.wunder.pets.pets

import java.util.UUID

import cats.implicits._
import com.wunder.pets.validations.Validations._
import com.wunder.pets.validations.{IsEmpty, NotGreaterThan, NotLessThan}
import eu.timepit.refined._
import eu.timepit.refined.collection.{MaxSize, MinSize, NonEmpty}
import eu.timepit.refined.numeric.Positive

import scala.concurrent.Future


case class Pet(id: UUID, name: Name, strength: Strength, speed: Speed, intelligence: Intelligence, integrity: Integrity)


trait PetAttribute extends Any {
  val value: Int
}

case class Name private(value: String) extends AnyVal

object Name {
  def from(v: String): Validated[Name] = {
    val notEmpty: Validated[String] =
      validate[String, NonEmpty](v, new IsEmpty("Pet name"))

    type LongerThan4 = MinSize[W.`5`.T]

    val minimumLength: Validated[String] =
      validate[String, LongerThan4](v, new NotGreaterThan("Pet name", 4))

    type ShorterThan20 = MaxSize[W.`20`.T]
    val maximumLength = validate[String, ShorterThan20](v, new NotLessThan("Pet name", 20))

    (minimumLength and notEmpty and maximumLength).map(new Name(_))
  }
}

case class Strength private(value: Int) extends AnyVal with PetAttribute

object Strength {
  def from(v: Int): Validated[Strength] =
    validate[Int, Positive](v, new NotGreaterThan("Pet strength", 0)).map(new Strength(_))
}

case class Speed private(value: Int) extends AnyVal with PetAttribute

object Speed {
  def from(v: Int): Validated[Speed] =
    validate[Int, Positive](v, new NotGreaterThan("Pet speed", 0)).map(new Speed(_))
}

case class Intelligence private(value: Int) extends AnyVal with PetAttribute

object Intelligence {
  def from(v: Int): Validated[Intelligence] =
    validate[Int, Positive](v, new NotGreaterThan("Pet intelligence", 0)).map(new Intelligence(_))
}

case class Integrity private(value: Int) extends AnyVal with PetAttribute

object Integrity {
  def from(v: Int): Validated[Integrity] =
    validate[Int, Positive](v, new NotGreaterThan("Pet integrity", 0)).map(new Integrity(_))
}

case class CreatePetForm(name: String, strength: Int, speed: Int, intelligence: Int, integrity: Int)

object Pet {
  def create(petRepo: PetRepository)(formData: CreatePetForm) = {
    val validatedPet = (
      // cleaning up the () => Pet() syntax would be nice.
      // bonus points if you can figure out a way to use curry/uncurry to
      // make this happen.

      Name.from(formData.name)
        |@| Strength.from(formData.strength)
        |@| Speed.from(formData.speed)
        |@| Intelligence.from(formData.intelligence)
        |@| Integrity.from(formData.integrity)
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

  def find(petRepository: PetRepository)(id: UUID): Future[Option[Pet]] =
    petRepository.find(id)
}


