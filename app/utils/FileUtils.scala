package utils

import java.io.{File, PrintWriter, FileNotFoundException, IOException}
import scala.io.Source
import scala.util.{Try, Success, Failure}

object FileUtils {

  def writeToFile(filePath: String, data: String): Try[Unit] = Try {
    val file = new File(filePath)
    if (filePath.length > 260) throw new IOException("File path too long")
    if (!file.getParentFile.exists() && !file.getParentFile.mkdirs()) throw new IOException("Failed to create parent directory")
    if (file.exists() && !file.canWrite) throw new IOException("File is locked or read-only")
    val writer = new PrintWriter(file)
    try writer.write(data) finally writer.close()
  } recoverWith {
    case e: IOException => Failure(new IOException(s"I/O error: ${e.getMessage}"))
    case e => Failure(new Exception(s"Failed to write to file: ${e.getMessage}"))
  }

  def readFromFile(filePath: String): Try[List[Int]] = Try {
    val file = new File(filePath)
    if (!file.exists()) throw new FileNotFoundException("File not found")
    if (file.length() == 0) throw new IOException("File is empty")
    if (!file.canRead) throw new IOException("File is locked or inaccessible")
    val source = Source.fromFile(file)
    try {
      val content = source.mkString.trim
      if (content.isEmpty) throw new IOException("File is empty")
      content.split(",").map { num =>
        try num.toInt
        catch {
          case _: NumberFormatException => throw new NumberFormatException(s"Invalid integer: $num")
        }
      }.toList
    } finally source.close()
  } recoverWith {
    case e: FileNotFoundException => Failure(new FileNotFoundException("File not found"))
    case e: NumberFormatException => Failure(new NumberFormatException("Invalid file content: Non-integer value found"))
    case e: IOException => Failure(new IOException(s"I/O error: ${e.getMessage}"))
    case e => Failure(new Exception(s"Failed to read from file: ${e.getMessage}"))
  }

  def clearFile(filePath: String): Try[Unit] = Try {
    val file = new File(filePath)
    if (!file.exists()) throw new FileNotFoundException("File not found")
    if (!file.canWrite) throw new IOException("File is locked or read-only")
    new PrintWriter(filePath).close()
  } recoverWith {
    case e: FileNotFoundException => Failure(new FileNotFoundException("File not found"))
    case e: IOException => Failure(new IOException("Permission denied or I/O error"))
    case e => Failure(new Exception(s"Failed to clear file: ${e.getMessage}"))
  }
}