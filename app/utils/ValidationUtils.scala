package utils

import models.FileRequest
import scala.util.{ Failure, Success, Try }

object ValidationUtils {

  def validateFileRequest(fileRequest: FileRequest): Try[Unit] = Try {
    if (fileRequest.start < 0 || fileRequest.end < 0)
      throw new IllegalArgumentException("Start and end must be non-negative integers")
    if (fileRequest.start > fileRequest.end)
      throw new IllegalArgumentException("Start must be less than or equal to end")
    if (fileRequest.end - fileRequest.start > 1000)
      throw new IllegalArgumentException("Range too large: Max allowed range is 1000")
    if (fileRequest.start == fileRequest.end) throw new IllegalArgumentException("Start and end cannot be the same")
  }
}
