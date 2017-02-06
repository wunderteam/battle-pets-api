package com.wunder.pets.pets

import java.util.UUID
import javax.inject.{Inject, Singleton}

import com.wunder.pets.validations.Validations
import com.wunder.pets.validations.Validations.ErrorMessages
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

    def id = column[UUID]("id", O.PrimaryKey)

    def strength = column[Strength]("strength")

    def name = column[Name]("name")

    def * = (id, strength, name) <> ((Pet.apply _).tupled, Pet.unapply)
  }

  private val pets = TableQuery[PetsTable]

  def create(pet: Pet): Future[Either[ErrorMessages, Pet]] = {
    val dbAction = db.run {
      (pets += pet).map(_ => Right(pet))
    }

    Validations.assureUnique[Pet](dbAction)
  }

  def list(): Future[Seq[Pet]] = db.run {
    pets.result
  }

  def find(id: UUID): Future[Option[Pet]] = db.run {
    pets.filter(_.id === id).result.headOption
  }
}
