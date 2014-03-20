package main.scala.tools

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:37
 */
object Sys {

  def property(prop: String):String = System.getProperty(prop).toLowerCase

  def architecture: String = property("os.arch")
  def osVersion: String = property("os.version")
  def os: String = property("os.name")

  def isMac: Boolean = os.indexOf("mac") >= 0
  def isWindows: Boolean = os.indexOf("win") >= 0
  def isUnix: Boolean = os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0 || os.indexOf("aix") > 0
  def isSolaris: Boolean = os.indexOf("sunos") >= 0

  def javaClassPath: String = property("java.class.path")
  def javaHome: String = property("java.home")
  def javaVendorName: String = property("java.vendor")
  def javaVendorUrl: String = property("java.vendor.url")
  def javaVersion: String = property("java.version")

  def lineSeparator: String = property("line.separator")
  def pathSeparator: String = property("path.separator")
  def fileSeparator: String = property("file.separator")

  def userWorkingDir: String = property("user.dir")
  def userHomeDir: String = property("user.home")
  def userName: String = property("user.name")


   def main(args: Array[String]) {

     println(Sys.os, Sys.osVersion,Sys.architecture)
     println(Sys.javaVersion)
     println(Sys.userName)


   }
}
