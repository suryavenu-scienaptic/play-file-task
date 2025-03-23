package utils

import play.api.libs.json._
import play.api.mvc.Results._
import play.api.mvc.Result
import scala.util.control.NonFatal
import models.FailureResponse
import java.io.{FileNotFoundException, IOException}

class ErrorHandler {

  def failureResponse(message: String): JsValue = {
    Json.toJson(FailureResponse("failed", message))
  }

  def handleFileErrors: PartialFunction[Throwable, Result] = {
    case e: FileNotFoundException => NotFound(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("Permission denied") => Forbidden(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File is empty") => UnprocessableEntity(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File is locked") => Conflict(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File path too long") => EntityTooLarge(failureResponse(e.getMessage))
    case e: NumberFormatException => UnprocessableEntity(failureResponse(e.getMessage))
    case e: IOException => InternalServerError(failureResponse(e.getMessage))
    case NonFatal(e) => InternalServerError(failureResponse(e.getMessage))
  }
}