package com.wunder.pets

import com.wunder.pets.validations.Validations.Validated

package object validations {
  implicit class ValidatedSyntax[T](original: Validated[T]) {
    def and(other: Validated[T]) =
      original.product(other).map[T]({ case (v, _) => v })
  }
}
