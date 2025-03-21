package utils

import play.api.mvc.Results._
import play.api.mvc.Result
import play.api.libs.json.{JsValue, Json}
import models.{FailureResponse, ValidationErrorResponse, ValidationError}
import java.io.{FileNotFoundException, IOException}

object ErrorHandler {

  def handleFileErrors: PartialFunction[Throwable, Result] = {
    case e: FileNotFoundException => NotFound(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("Permission denied") => Forbidden(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File is empty") => UnprocessableEntity(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File is locked") => Conflict(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File path too long") => EntityTooLarge(failureResponse(e.getMessage))
    case e: NumberFormatException => UnprocessableEntity(failureResponse(e.getMessage))
    case e: IOException => InternalServerError(failureResponse(e.getMessage))
    case e => InternalServerError(failureResponse(e.getMessage))
  }

  def failureResponse(message: String, errors: List[ValidationError] = List.empty): JsValue = {
    if (errors.isEmpty) Json.toJson(FailureResponse("failed", message))
    else Json.toJson(ValidationErrorResponse("failed", message, errors))
  }
}