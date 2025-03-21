package services

import play.api.Configuration
import utils.FileUtils

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FileService @Inject()(config: Configuration)(implicit ec: ExecutionContext) {

  private val filePath = config.getOptional[String]("file.path").getOrElse(throw new RuntimeException("File path not configured"))

  def saveToFile(start: Int, end: Int): Future[Unit] = Future {
    FileUtils.writeToFile(filePath, (start to end).mkString(",")).get
  }

  def fetchFromFile: Future[List[Int]] = Future {
    FileUtils.readFromFile(filePath).get
  }

  def deleteFromFile: Future[Unit] = Future {
    FileUtils.clearFile(filePath).get
  }
}