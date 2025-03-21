package controllers

import models._
import play.api.libs.json._
import play.api.mvc._
import services.FileService
import utils.{ErrorFormatter, ValidationUtils}

import java.io.{FileNotFoundException, IOException}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class FileController @Inject()(cc: ControllerComponents, fileService: FileService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  protected def failureResponse(message: String, errors: List[ValidationError] = List.empty): JsValue = {
    if (errors.isEmpty) Json.toJson(FailureResponse("failed", message))
    else Json.toJson(ValidationErrorResponse("failed", message, errors))
  }

  def saveToFile = Action(parse.json).async { implicit request =>
    request.body.validate[FileRequest] match {
      case JsSuccess(fileRequest, _) =>
        ValidationUtils.validateFileRequest(fileRequest) match {
          case Success(_) =>
            fileService.saveToFile(fileRequest.start, fileRequest.end).map { _ =>
              Ok(Json.toJson(SaveToFileResponse("success")))
            }.recover(handleFileErrors)
          case Failure(e) => Future.successful(BadRequest(failureResponse(e.getMessage)))
        }
      case JsError(errors) =>
        Future.successful(BadRequest(failureResponse("Validation failed", ErrorFormatter.formatJsError(errors))))
    }
  }

  def fetchFromFile = Action.async {
    fileService.fetchFromFile.map { data =>
      Ok(Json.toJson(FetchFromFileResponse(data)))
    }.recover(handleFileErrors)
  }

  def deleteFromFile = Action.async {
    fileService.deleteFromFile.map { _ =>
      Ok(Json.toJson(DeleteFromFileResponse(true)))
    }.recover(handleFileErrors)
  }

  private def handleFileErrors: PartialFunction[Throwable, Result] = {
    case e: FileNotFoundException => NotFound(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("Permission denied") => Forbidden(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File is empty") => UnprocessableEntity(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File is locked") => Conflict(failureResponse(e.getMessage))
    case e: IOException if e.getMessage.contains("File path too long") => EntityTooLarge(failureResponse(e.getMessage))
    case e: NumberFormatException => UnprocessableEntity(failureResponse(e.getMessage))
    case e: IOException => InternalServerError(failureResponse(e.getMessage))
    case e => InternalServerError(failureResponse(e.getMessage))
  }
}