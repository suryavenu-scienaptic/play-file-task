package utils

import java.io.{File, PrintWriter, FileNotFoundException, IOException}
import scala.io.Source
import scala.util.{Try, Success, Failure}

object FileUtils {

  def writeToFile(filePath: String, data: String): Try[Unit] = Try {
    val file = new File(filePath)
    file.getParentFile.mkdirs()
    val writer = new PrintWriter(file)
    try writer.write(data) finally writer.close()
  } recover {
    case e: IOException => throw new IOException("Permission denied")
    case e => throw new Exception(e.getMessage)
  }

  def readFromFile(filePath: String): Try[List[Int]] = Try {
    val source = Source.fromFile(filePath)
    try {
      val content = source.mkString.trim
      if (content.isEmpty) List.empty[Int]
      else content.split(",").map(_.toInt).toList
    } finally source.close()
  } recover {
    case _: FileNotFoundException => throw new FileNotFoundException("File not found")
    case _: NumberFormatException => throw new NumberFormatException("Invalid file content: Non-integer value found")
    case e => throw new Exception(e.getMessage)
  }

  def clearFile(filePath: String): Try[Unit] = Try {
    val file = new File(filePath)
    if (file.exists()) {
      new PrintWriter(filePath).close()
    } else {
      throw new FileNotFoundException("File not found")
    }
  } recover {
    case e: FileNotFoundException => throw new FileNotFoundException("File not found")
    case e => throw new Exception(e.getMessage)
  }
}

