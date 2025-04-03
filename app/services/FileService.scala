package services

import play.api.Configuration
import utils.FileUtils
import javax.inject._
import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }
import play.api.Logger

@Singleton
class FileService @Inject()(config: Configuration)(implicit ec: ExecutionContext) {

  private val logger = Logger(this.getClass)
  private val filePath =
    config.getOptional[String]("file.path").getOrElse(throw new RuntimeException("File path not configured"))

  def saveToFile(start: Int, end: Int): Future[Unit] = Future {
    FileUtils.writeToFile(filePath, start.to(end).mkString(",")) match {
      case Success(_) => ()
      case Failure(e) =>
        logger.error(s"Failed to save to file: ${e.getMessage}", e)
        throw e
    }
  }

  def fetchFromFile: Future[List[Int]] = Future {
    FileUtils.readFromFile(filePath) match {
      case Success(data) => data
      case Failure(e) =>
        logger.error(s"Failed to fetch from file: ${e.getMessage}", e)
        throw e
    }
  }

  def deleteFromFile: Future[Unit] = Future {
    FileUtils.clearFile(filePath) match {
      case Success(_) => ()
      case Failure(e) =>
        logger.error(s"Failed to delete from file: ${e.getMessage}", e)
        throw e
    }
  }
}
