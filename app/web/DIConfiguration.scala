package com.wunder.pets.web

import javax.inject.Inject

import com.google.inject.{AbstractModule, Provides}
import com.wunder.pets.entity.Entity.{All, Create, Find}
import com.wunder.pets.pets._

import scala.concurrent.ExecutionContext

class DIConfiguration extends AbstractModule {

  override def configure() = {
    bind(classOf[PetRepository]).to(classOf[PersistentPetRepository])
  }

  @Provides
  def provideCreatePet(@Inject petRepository: PetRepository)(implicit ec: ExecutionContext): Create[CreatePetForm, Pet] =
    Pet.create(petRepository)

  @Provides
  def provideFindPet(@Inject petRepository: PetRepository)(implicit ec: ExecutionContext): Find[Pet] =
    Pet.find(petRepository)

  @Provides
  def provideAllPets(@Inject petRepository: PetRepository)(implicit ec: ExecutionContext): All[Pet] =
    Pet.all(petRepository)
}
