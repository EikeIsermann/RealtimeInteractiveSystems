package main.scala.io

import scala.reflect.ClassTag
import java.io.FileInputStream
import test.FileReader
import scala.io.Source


/**
 * Created by Christian Treffs
 * Date: 10.03.14 10:17
 */
object File {

  def getExtension(filePath: String, withDot: Boolean): String = getExtension(load(filePath), withDot)
  def getExtension(file: java.io.File, withDot: Boolean = false): String = {
    val fn: String = file.getName.toString
    val i: Int = if(withDot) fn.lastIndexOf(".") else fn.lastIndexOf(".")+1
    fn.substring(i).toLowerCase
  }



  def getPath(file: java.io.File): String = {
    val pathWithFile = file.getAbsolutePath
    val name = file.getName
    val path = pathWithFile.reverse.replaceFirst(name.reverse, "").reverse
    path
  }

  def load(filePath: String): java.io.File = {
    val f = new java.io.File(filePath)
    if(!f.exists) {
      throw new IllegalArgumentException("file does not exist '"+filePath+"'")
    }
    if(!f.canRead) {
      throw new IllegalArgumentException("can not read file '"+filePath+"'")
    }
    f
  }


  def loadAsArray[T : ClassTag](filePath: String): Array[T] =  loadAsArray[T](load(filePath))
  def loadAsArray[T : ClassTag](f: java.io.File): Array[T] = {
    val ret : Array[T] = new Array[T](0)

     ret match {
      case a: Array[Byte] => Source.fromFile(f).getLines().map(_.getBytes).toList.flatten.toArray.asInstanceOf[Array[T]]
      case _ => throw new IllegalArgumentException("cant load as "+ret.getClass)

    }


  }



}
