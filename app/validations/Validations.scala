package com.wunder.pets.validations

import cats.data
import cats.data.{NonEmptyList, ValidatedNel}
import eu.timepit.refined.api.Validate
import eu.timepit.refined.refineV
import org.postgresql.util.PSQLException

object Validations {
  type Validated[T] = ValidatedNel[ValidationError, T]
  type Errors = NonEmptyList[ValidationError]
  type ErrorMessages = Seq[String]
  type WithErrorMessages[T] = Either[ErrorMessages, T]

  def validate[VALUE, VALIDATION](v: VALUE, e: ValidationError)
                                 (implicit validation: Validate[VALUE, VALIDATION]): Validated[VALUE] = {
    val validated: Either[Errors, VALUE] = refineV[VALIDATION](v)
      .left.map(_ => NonEmptyList(e, Nil))
      .right.map(_.value)
    data.Validated.fromEither(validated)
  }

  def assureUnique[T]: PartialFunction[Throwable, WithErrorMessages[T]] = {
    case e: PSQLException => {
      toErrorMessages(dbError(e))
    }
  }

  def dbError(e: PSQLException): Errors = {
    def isUniquenessViolation(e: PSQLException) = e.getServerErrorMessage.getDetail.matches(".* already exists.")

    val error = if (isUniquenessViolation(e)) {
      new DuplicateValue(e)
    } else {
      new GeneralError(e.getMessage)
    }
    NonEmptyList(error, Nil)
  }

  def toErrorMessages[T](errors: Errors): WithErrorMessages[T] = Left(errors.map(_.message).toList)
}
