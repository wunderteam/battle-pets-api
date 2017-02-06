package com.wunder.pets.validations

import cats.data.{NonEmptyList, Validated, ValidatedNel}
import com.wunder.pets.validations.Validations.Validation
import org.postgresql.util.PSQLException

import scala.concurrent.{ExecutionContext, Future}

object Validations {
  type Validation[T] = ValidatedNel[ValidationError, T]
  type Errors = NonEmptyList[ValidationError]
  type ErrorMessages = Seq[String]
  type WithValidationErrors[T] = Either[ErrorMessages, T]

  implicit class ErrorsWithMessages(errors: Errors) {
    def messages: ErrorMessages = errors.map(_.message).toList
  }

  def assureUnique[T](f: Future[WithValidationErrors[T]])(implicit executionContext: ExecutionContext) = f.recoverWith {
    case e: PSQLException => {
      val errors: Errors = dbError(e)
      Future.successful(Left(errors.messages))
    }
  }

  def passed[T](attribute: T) = Validated.valid(attribute)

  def failed(error: ValidationError) = Validated.invalidNel(error)

  def stringifyErrors(errors: Errors) = errors.map(_.message).toList.mkString(", ")

  def dbError(e: PSQLException): Errors = {
    def isUniquenessViolation(e: PSQLException) = e.getServerErrorMessage.getDetail.matches(".* already exists.")

    val error = if (isUniquenessViolation(e)) {
      new DuplicateValue(e)
    } else {
      new GeneralError(e.getMessage)
    }
    NonEmptyList(error, Nil)
  }

  def handleValidationErrors[T](errors: Errors): Future[WithValidationErrors[T]] = Future.successful(Left(errors.messages))
}


// TODO: determine one parameter list vs two (allows currying)
trait NonEmptyString {
  def validate(field: String)(value: String): Validation[String] =
    if (!value.isEmpty) {
      Validations.passed(value)
    } else {
      Validations.failed(new IsEmpty(field))
    }
}

trait GreaterThan[T] {
  def validate(field: String, lowerBound: T)(value: T)(implicit ord: Ordering[T]): Validation[T] =
    if (ord.gt(value, lowerBound)) {
      Validations.passed(value)
    } else {
      Validations.failed(new NotGreaterThan(field, lowerBound))
    }
}
