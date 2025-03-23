package models

import play.api.libs.json._

case class FetchFromFileResponse(res: List[Int])

object FetchFromFileResponse {
  implicit val writes: Writes[FetchFromFileResponse] = Json.writes[FetchFromFileResponse]
}

case class SaveToFileResponse(status: String, message: String)

object SaveToFileResponse {
  implicit val writes: Writes[SaveToFileResponse] = Json.writes[SaveToFileResponse]
}

case class DeleteFromFileResponse(success: Boolean, message: String)

object DeleteFromFileResponse {
  implicit val writes: Writes[DeleteFromFileResponse] = Json.writes[DeleteFromFileResponse]
}

case class FailureResponse(status: String, message: String)

object FailureResponse {
  implicit val writes: Writes[FailureResponse] = Json.writes[FailureResponse]
}

case class ValidationError(field: String, message: String)

object ValidationError {
  implicit val writes: Writes[ValidationError] = Json.writes[ValidationError]
}
