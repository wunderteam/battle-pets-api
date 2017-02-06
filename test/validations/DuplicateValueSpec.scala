package validations

import com.wunder.pets.validations.DuplicateValue
import org.postgresql.util.{PSQLException, ServerErrorMessage}
import org.scalatest.{MustMatchers, WordSpec}

class DuplicateValueSpec extends WordSpec with MustMatchers {
  "message" should {
    "extract field and value" in {
      val errorMessage = "SERROR\u0000VERROR\u0000C23505\u0000Mduplicate key value violates unique constraint \"name_unique_idx\"\u0000DKey (someField)=(Foo) already exists.\u0000spublic\u0000tpets\u0000nname_unique_idx\u0000Fnbtinsert.c\u0000L433\u0000R_bt_check_unique\u0000\u0000"
      val serverError = new ServerErrorMessage(errorMessage, 0)
      val originalError = new PSQLException(serverError)

      val result = new DuplicateValue(originalError).message

      result.mustEqual("someField has a duplicate value of Foo")

    }
  }

}
