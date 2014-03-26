package main.scala.systems.gfx

import java.io.File
import javax.imageio.ImageIO
import java.awt.image.{AffineTransformOp, DataBufferByte, BufferedImage}
import org.lwjgl.opengl._
import java.nio.{ByteOrder, ByteBuffer}
import org.lwjgl.opengl.GL11._
import main.scala.tools.DC
import scala.collection.mutable
import java.awt.geom.AffineTransform
import main.scala.io.FileIO


/**
 * Created by Christian Treffs
 * Date: 05.03.14 10:35
 */
object Texture {

  //TODO: remove?
  final var textures: mutable.ListBuffer[Texture] = mutable.ListBuffer.empty[Texture]

  def load(filePath: String, flipped: Boolean = true): Texture = {
    val t = new Texture(new File(filePath), flipped)
    textures += t
    t
  }

  /**
   * call this every time you want to use the texture
   */
  def bind(texId: Int) {
    glEnable(GL_TEXTURE_2D)
    //glEnable(GL_BLEND)
    //glBlendFunc(GL_ONE, GL_ONE)
    glBindTexture(GL_TEXTURE_2D, texId)
  }

}

sealed class Texture(texFile: File, flipped: Boolean) {

  final private var texBufferedImage: BufferedImage = null
  final var texId: Int = -1
  final var pixelFormat: Int = -1
  final val internalFormat: Int = GL11.GL_RGBA
  //<-- most common most general transparency format - don't change
  final var dataType: Int = -1
  final var width: Int = -1
  final var height: Int = -1
  final var format: String = "unknown"
  final var channels: Int = -1
  final var imageSize: Int = -1
  final var imageDataSize: Int = -1
  final var imageData: Array[Byte] = null
  final var imageBuffer: ByteBuffer = null

  final var aspect: Float = -1

  private val start = System.currentTimeMillis()

  texBufferedImage = ImageIO.read(texFile)
  if (texBufferedImage == null) {
    throw new Exception("BufferedImage is null '" + texFile.getAbsoluteFile + "'")
  }

  format = FileIO.getExtension(texFile)
  width = texBufferedImage.getWidth
  height = texBufferedImage.getHeight
  channels = texBufferedImage.getRaster.getNumBands
  imageSize = width * height
  imageDataSize = channels * imageSize

  aspect = height.toFloat/width.toFloat


  if(flipped) {
  // Flip the image vertically
  val tx = AffineTransform.getScaleInstance(1, -1)
  tx.translate(0, -texBufferedImage.getHeight(null))
  val op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR)
  texBufferedImage = op.filter(texBufferedImage, null)
  }

  imageData = texBufferedImage.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData

  texBufferedImage.getType match {
    case BufferedImage.TYPE_3BYTE_BGR =>
      pixelFormat = GL12.GL_BGR //<-- most
      dataType = GL11.GL_UNSIGNED_BYTE

    case BufferedImage.TYPE_INT_RGB =>
      pixelFormat = GL11.GL_RGB
      dataType = GL11.GL_INT

    case BufferedImage.TYPE_4BYTE_ABGR =>
      pixelFormat = GL12.GL_BGRA // <-- some
      dataType = GL11.GL_UNSIGNED_BYTE
      imageData = reorderAlphaChannel(imageData, imageSize, channels)

    case BufferedImage.TYPE_INT_ARGB =>
      pixelFormat = GL11.GL_RGBA
      dataType = GL11.GL_INT
      imageData = reorderAlphaChannel(imageData, imageSize, channels)

    case _ => throw new IllegalArgumentException("Unsupported image type " + texBufferedImage.getType)

  }


  if (imageData.length != imageDataSize) {
    throw new IllegalArgumentException("Something went wrong during data conversion")
  }

  imageBuffer = ByteBuffer.allocateDirect(imageDataSize)
  imageBuffer.order(ByteOrder.nativeOrder())
  imageBuffer.put(imageData)

  imageBuffer.rewind() // necessary!

  // OGL

  glEnable(GL_TEXTURE_2D) //this is necessary
  texId = glGenTextures()
  Texture.bind(texId) //this is necessary


  // Set parameters
  //glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
  //glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
  //Setup wrap mode
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)

  glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
  glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR)


  // Load the texture image
  glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, pixelFormat, dataType, imageBuffer)

  GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)

  private val end = System.currentTimeMillis()
  DC.log("Texture " + texId + " '" + texFile.getName + "' loaded in " + (end - start) + "ms")



  def test(posX: Float, posY: Float) {
    Texture.bind(texId)

    GL11.glBegin(GL11.GL_QUADS)

    GL11.glTexCoord2f(0, 0)
    GL11.glVertex2f(posX, posY)

    GL11.glTexCoord2f(1, 0)
    GL11.glVertex2f(posX+width, posY)

    GL11.glTexCoord2f(1, 1)
    GL11.glVertex2f(posX+width, posY+height)

    GL11.glTexCoord2f(0, 1)
    GL11.glVertex2f(posX, posY+height)

    GL11.glEnd()

  }



  /**
   * reorder the beginning alpha channel to a trailing alpha channel
   * @param dataBuffer the image data
   * @param imageSize the width*height of the image
   * @param channels the channel size - usually 4
   * @return
   */
  private def reorderAlphaChannel(dataBuffer: Array[Byte], imageSize: Int, channels: Int): Array[Byte] = {
    val retArr: Array[Byte] = new Array[Byte](dataBuffer.length)
    for (i <- 0 until imageSize) {
      for (j <- 0 until channels) {
        if (j + 1 == channels) retArr(channels * i + j) = dataBuffer(channels * i)
        else retArr(channels * i + j) = dataBuffer(channels * i + (j + 1))
      }
    }
    retArr
  }
}

