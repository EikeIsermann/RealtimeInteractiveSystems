package main.scala.tools

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:37
 */
object Sys {


  def os: String = System.getProperty("os.name").toLowerCase

  def isMac: Boolean = os.indexOf("mac") >= 0
  def isWindows: Boolean = os.indexOf("win") >= 0
  def isUnix: Boolean = os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0
  def isSolaris: Boolean = os.indexOf("sunos") >= 0


   def main(args: Array[String]) {
     println(Sys.os)
     println(Sys.isMac)
     println(Sys.isWindows)
     println(Sys.isUnix)
     println(Sys.isSolaris)
   }
}
