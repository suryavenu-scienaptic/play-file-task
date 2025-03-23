package controllers

import models._
import play.api.libs.json._
import play.api.mvc._
import services.FileService
import utils.{ErrorFormatter, ErrorHandler, ValidationUtils}
import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.Logger

@Singleton
class FileController @Inject()(cc: ControllerComponents, fileService: FileService, errorHandler: ErrorHandler)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val logger = Logger(this.getClass)

  def saveToFile = Action(parse.json).async { implicit request =>
    request.body.validate[FileRequest] match {
      case JsSuccess(fileRequest, _) =>
        ValidationUtils.validateFileRequest(fileRequest) match {
          case Success(_) =>
            fileService.saveToFile(fileRequest.start, fileRequest.end).map { _ =>
              Ok(Json.toJson(SaveToFileResponse("success", "File saved successfully")))
            }.recover(errorHandler.handleFileErrors)
          case Failure(e) =>
            logger.warn(s"Validation failed: ${e.getMessage}")
            Future.successful(BadRequest(errorHandler.failureResponse(e.getMessage)))
        }
      case JsError(errors) =>
        val errorMessage = ErrorFormatter.formatJsError(errors)
        logger.warn(s"JSON validation failed: $errorMessage")
        Future.successful(BadRequest(errorHandler.failureResponse(s"Validation failed: $errorMessage")))
    }
  }

  def fetchFromFile = Action.async {
    fileService.fetchFromFile.map { data =>
      Ok(Json.toJson(FetchFromFileResponse(data)))
    }.recover(errorHandler.handleFileErrors)
  }

  def deleteFromFile = Action.async {
    fileService.deleteFromFile.map { _ =>
      Ok(Json.toJson(DeleteFromFileResponse(true, "File deleted successfully")))
    }.recover(errorHandler.handleFileErrors)
  }
}