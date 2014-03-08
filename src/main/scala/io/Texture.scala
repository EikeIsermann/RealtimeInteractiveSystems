package main.scala.io

import java.awt.Dimension
import java.io.File
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11

/**
 * Created by Christian Treffs
 * Date: 05.03.14 10:35
 */
object Texture {

  final val prefMaxDimensions = new Dimension(1024,1024)
  final val prefMaxSize = 1024 //KB

  def load(identifier: String, filePath: String): Texture = {

    new Texture(identifier, new File(filePath))
  }
}

sealed class Texture(identifier: String, file: File) {


}

