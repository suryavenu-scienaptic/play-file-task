package utils

import play.api.libs.json._
import models.ValidationError

object ErrorFormatter {

  def formatJsError(errors: Seq[(JsPath, Seq[JsonValidationError])]): List[ValidationError] = {
    errors.flatMap { case (path, validationErrors) =>
      validationErrors.map { error =>
        val field = path.toString().replace("obj.", "")
        val message = error.message match {
          case "error.expected.jsnumber" => "Expected a number"
          case "error.path.missing"      => "This field is required"
          case _                        => error.message
        }
        ValidationError(field, message)
      }
    }.toList
  }
}