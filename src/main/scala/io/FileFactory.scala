package main.scala.io



/**
 * Created by Christian Treffs
 * Date: 10.03.14 10:17
 */
object FileFactory {

  def getExtension(file: java.io.File, withDot: Boolean = false): String = {
    val fn: String = file.getName.toString
    val i: Int = if(withDot) fn.lastIndexOf(".") else fn.lastIndexOf(".")+1
    fn.substring(i).toLowerCase
  }

  def getExtension(filePath: String, withDot: Boolean): String = getExtension(new java.io.File(filePath), withDot)

}
