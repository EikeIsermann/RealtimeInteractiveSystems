package main.scala.io

import scala.reflect.ClassTag
import java.io.{FileInputStream, BufferedInputStream, File}
import scala.io.Source


/**
 * Created by Christian Treffs
 * Date: 10.03.14 10:17
 */
object FileIO {

  def getExtension(filePath: String, withDot: Boolean): String = getExtension(load(filePath), withDot)
  def getExtension(file: java.io.File, withDot: Boolean = false): String = {
    val fn: String = file.getName
    val i: Int = if(withDot) fn.lastIndexOf(".") else fn.lastIndexOf(".")+1
    fn.substring(i).toLowerCase
  }

  def getName(file: File): String = file.getName.split('.')(0)


  def getPath(file: java.io.File): String = {
    val pathWithFile = file.getAbsolutePath
    val name = file.getName
    val path = pathWithFile.reverse.replaceFirst(name.reverse, "").reverse
    path
  }

  def loadAll(extension: String, pathToDirectory: String): Seq[java.io.File] = {
    val dir: File = load(pathToDirectory)
    if(dir.isDirectory && dir.exists()) {
      if(dir.canRead) {
        dir.listFiles().filter(f => getExtension(f).equals(extension.toLowerCase))
      }  else {
        throw new IllegalArgumentException("can not read from dir '"+pathToDirectory+"'")
      }
    } else {
      throw new IllegalArgumentException("path is not a directory '"+pathToDirectory+"'")
    }
  }

  implicit def loadInto(filePath: String): BufferedInputStream = new BufferedInputStream(new FileInputStream(load(filePath)))

  implicit def load(filePath: String): java.io.File = {
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
