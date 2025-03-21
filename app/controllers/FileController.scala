package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import services.FileService
import scala.concurrent.ExecutionContext
import models._
import utils.{ValidationUtils, ErrorFormatter}
import scala.concurrent.Future
import scala.util.{Try, Success, Failure}
import java.io.{IOException, FileNotFoundException}

@Singleton
class FileController @Inject()(cc: ControllerComponents, fileService: FileService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  // Make failureResponse protected if it needs to be accessible to subclasses
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
    case e: IOException => Forbidden(failureResponse(e.getMessage))
    case e: NumberFormatException => UnprocessableEntity(failureResponse(e.getMessage))
    case e => InternalServerError(failureResponse(e.getMessage))
  }
}