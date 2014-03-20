package main.scala.tools

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:37
 */
object Sys {


  def os: Symbol = {
    val os = System.getProperty("os.name")
    Symbol(os.toLowerCase)

  }

  def isMac: Boolean = false
  def isWindows: Boolean = false
  def isLinux: Boolean = false


   def main() {
     println(Sys.os)
   }
}
