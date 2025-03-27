package controllers

import io.swagger.annotations._
import javax.inject._
import models._
import play.api.libs.json._
import play.api.mvc._
import services.FileService
import utils.{ErrorFormatter, ErrorHandler, ValidationUtils}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}
import play.api.Logger

@Singleton
@Api(value = "/file", description = "Operations related to file management")
class FileController @Inject()(cc: ControllerComponents, fileService: FileService, errorHandler: ErrorHandler)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  private val logger = Logger(this.getClass)

  @ApiOperation(value = "Save data to file", response = classOf[SaveToFileResponse], httpMethod = "POST")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "File saved successfully"),
    new ApiResponse(code = 400, message = "Validation failed"),
    new ApiResponse(code = 500, message = "Internal server error", response = classOf[FailureResponse])
  ))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "File request payload", required = true, dataType = "models.FileRequest", paramType = "body")
  ))
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

  @ApiOperation(value = "Fetch data from file", response = classOf[FetchFromFileResponse], httpMethod = "GET")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "File data retrieved successfully"),
    new ApiResponse(code = 500, message = "Internal server error", response = classOf[FailureResponse])
  ))
  def fetchFromFile = Action.async {
    fileService.fetchFromFile.map { data =>
      Ok(Json.toJson(FetchFromFileResponse(data)))
    }.recover(errorHandler.handleFileErrors)
  }

  @ApiOperation(value = "Delete file", response = classOf[DeleteFromFileResponse], httpMethod = "DELETE")
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "File deleted successfully"),
    new ApiResponse(code = 500, message = "Internal server error", response = classOf[FailureResponse])
  ))
  def deleteFromFile = Action.async {
    fileService.deleteFromFile.map { _ =>
      Ok(Json.toJson(DeleteFromFileResponse(true, "File deleted successfully")))
    }.recover(errorHandler.handleFileErrors)
  }
}
