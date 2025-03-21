package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import services.FileService
import play.api.libs.json.Json

@Singleton
class HomeController @Inject()(
                                val controllerComponents: ControllerComponents,
                                fileService: FileService
                              )(implicit ec: ExecutionContext) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.apiDemo())
  }
}