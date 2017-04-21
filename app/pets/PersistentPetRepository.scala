package com.wunder.pets.pets

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.wunder.pets.validations.Validations
import com.wunder.pets.validations.Validations.{ErrorMessages, WithErrorMessages}
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersistentPetRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends PetRepository {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class PetsTable(tag: Tag) extends Table[Pet](tag, "pets") {

    implicit val nameColumnType = MappedColumnType.base[Name, String](_.value, Name(_))
    implicit val strengthColumnType = MappedColumnType.base[Strength, Int](_.value, Strength(_))
    implicit val speedColumnType = MappedColumnType.base[Speed, Int](_.value, Speed(_))
    implicit val intelligenceColumnType = MappedColumnType.base[Intelligence, Int](_.value, Intelligence(_))
    implicit val integrityColumnType = MappedColumnType.base[Integrity, Int](_.value, Integrity(_))



    def id = column[UUID]("id", O.PrimaryKey)

    def strength = column[Strength]("strength")
    def speed = column[Speed]("speed")
    def intelligence = column[Intelligence]("intelligence")
    def integrity = column[Integrity]("integrity")
    def name = column[Name]("name")

    def * = (id, name, strength, speed, intelligence, integrity) <> ((Pet.apply _).tupled, Pet.unapply)
  }

  private val pets = TableQuery[PetsTable]

  def create(pet: Pet): Future[WithErrorMessages[Pet]] = {
    val dbAction = db.run {
      // TODO: Does this leak that WithValidationErrors is an Either under the hood?
      // Do we care?
      // Should we run validations here as well?
      (pets += pet).map(_ => Right(pet))
    }

    dbAction.recover(Validations.assureUnique)
  }

  def list(): Future[Seq[Pet]] = db.run {
    pets.result
  }

  def find(id: UUID): Future[Option[Pet]] = db.run {
    pets.filter(_.id === id).result.headOption
  }
}
