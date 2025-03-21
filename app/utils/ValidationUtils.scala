package utils

import models.FileRequest
import scala.util.{Try, Success, Failure}

object ValidationUtils {

  def validateFileRequest(fileRequest: FileRequest): Try[Unit] = {
    if (fileRequest.start < 0 || fileRequest.end < 0) Failure(new IllegalArgumentException("Start and end must be non-negative integers"))
    else if (fileRequest.start > fileRequest.end) Failure(new IllegalArgumentException("Start must be less than or equal to end"))
    else Success(())
  }
}