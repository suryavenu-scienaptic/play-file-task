import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object stockapp extends RootModule with PlayModule {

  def scalaVersion = "2.12.10"
  def playVersion = "2.8.1"
  def twirlVersion = "2.0.1"

  object test extends PlayTests
}
