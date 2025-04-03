package utils

import play.api.libs.json._

object ErrorFormatter {

  def formatJsError(errors: Seq[(JsPath, Seq[JsonValidationError])]): String = {
    val errorMessages = errors
      .flatMap {
        case (path, validationErrors) =>
          validationErrors.map { error =>
            val field = path.toString().replace("obj.", "")
            val message = error.message match {
              case "error.expected.jsnumber" => s"Field '$field' must be a number"
              case "error.path.missing"      => s"Field '$field' is required"
              case _                         => s"Field '$field': ${error.message}"
            }
            message
          }
      }
      .mkString(", ")

    errorMessages
  }
}
