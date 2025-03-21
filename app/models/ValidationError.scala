package models

import play.api.libs.json._

case class ValidationErrorResponse(status: String, message: String, errors: List[ValidationError])

object ValidationErrorResponse {
  implicit val writes: Writes[ValidationErrorResponse] = Json.writes[ValidationErrorResponse]
}

case class ValidationError(field: String, message: String)

object ValidationError {
  implicit val writes: Writes[ValidationError] = Json.writes[ValidationError]
}