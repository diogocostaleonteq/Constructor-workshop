package com.leonteq.workshop.chat.server.validators

import cats.effect.IO
import cats.syntax.applicative._
import com.leonteq.constructor.utils.validation._
import com.leonteq.constructor.utils.validation.error.ErrorType
import com.leonteq.constructor.utils.validation.fields.{userName => usernameField}
import com.leonteq.constructor.utils.validation.implicits._
import com.leonteq.constructor.utils.validation.EndoValidator
import com.leonteq.constructor.utils.validation.ValidationResult
import com.leonteq.workshop.chat.server.validators.UsernameValidator.UsernameTakenError

class UsernameValidator(onlineUsers: List[String]) extends EndoValidator[IO, String] {

  override def apply(username: String): IO[ValidationResult[String]] =
    if (onlineUsers.contains(username))
      ValidationError(
        ErrorType.ConditionBreach,
        usernameField,
        UsernameTakenError
      ).invalid.pure[IO]
    else
      username.valid.pure[IO]
}

object UsernameValidator {
  val UsernameTakenError = "Username is already taken"
}
