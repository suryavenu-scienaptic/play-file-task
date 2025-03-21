package models

import play.api.libs.json._

case class FileRequest(start: Int, end: Int)

object FileRequest {
  implicit val reads: Reads[FileRequest] = Json.reads[FileRequest]
}