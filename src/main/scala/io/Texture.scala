package main.scala.io

import java.awt.Dimension
import java.io.File
import javax.imageio.ImageIO
import java.awt.image.{DataBufferByte, BufferedImage}
import org.lwjgl.opengl._
import java.nio.{ByteOrder, ByteBuffer}
import org.lwjgl.opengl.GL11._
import main.scala.tools.DC
import scala.collection.mutable


/**
 * Created by Christian Treffs
 * Date: 05.03.14 10:35
 */
object Texture {

  final val prefMaxDimensions = new Dimension(1024, 1024)
  final val prefMaxSize = 1024
  //KB
  final var textures: mutable.ListBuffer[Texture] = mutable.ListBuffer.empty[Texture]


  var windowWidth = 0
  var xPos = 10
  var yPos = 10

  var offsetX = 0
  var offsetY = 0

  var i: Int = 0
  var l: Int = -1

  def load(filePath: String): Texture = {
    val t = new Texture(new File(filePath))
    textures += (t)
    t
  }

  var currentTexture: Texture = null

  def initGL(width: Int, height: Int) {
    windowWidth = width
    try {
      Display.setDisplayMode(new DisplayMode(width, height))
      Display.create()
      Display.setVSyncEnabled(true)
    }

    GL11.glEnable(GL11.GL_TEXTURE_2D)


    GL11.glClearColor(0.2f, 0.8f, 0.1f, 0.0f)

    // enable alpha blending
    GL11.glEnable(GL11.GL_BLEND)
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

    GL11.glViewport(0, 0, width, height)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)

    GL11.glMatrixMode(GL11.GL_PROJECTION)
    GL11.glLoadIdentity();
    GL11.glOrtho(0, width, height, 0, 1, -1)
    GL11.glMatrixMode(GL11.GL_MODELVIEW)
  }


  /**
   * Initialise resources
   */
  def init() {

    // Enable depth testing.
    glEnable(GL11.GL_DEPTH_TEST)

    // load texture from PNG file

    //texture = Texture.load("TankChassisTread.png", "/Users/ctreffs/Desktop/3DModels/T-90/TankChassisTread.png")
    //texture = Texture.load("SkyBox.jpg", "/Users/ctreffs/Desktop/3DModels/SkyBox/SkyBox.jpg")

    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadEW.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadNE.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadNEWS.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadNS.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadNW.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadPLAZA.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadSE.jpg")
    Texture.load("/Users/ctreffs/Desktop/3DModels/roads/roadSW.jpg")


  }


  /**
   * draw a quad with the image on it
   */


  def render() {
    //Color.white.bind();

    offsetX = 0
    offsetY = 0
    // Clear the screen and depth buffer
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)

    textures.toList foreach (
      t => {


        t.bind()
        GL11.glBegin(GL11.GL_QUADS)



        //bottom left
        GL11.glTexCoord2f(0, 0)
        GL11.glVertex2f(xPos + offsetX, yPos + offsetY)


        GL11.glTexCoord2f(1, 0)
        GL11.glVertex2f(xPos + t.width + offsetX, yPos + offsetY)

        GL11.glTexCoord2f(1, 1)
        GL11.glVertex2f(xPos + t.width + offsetX, yPos + t.height + offsetY)

        GL11.glTexCoord2f(0, 1)
        GL11.glVertex2f(xPos + offsetX, yPos + t.height + offsetY)



        GL11.glEnd()

        //t.unbind()
        offsetX = offsetX + t.width + 2
        if (offsetX + t.width >= windowWidth) {
          offsetY = offsetY + t.height + 2
          offsetX = 0
        }

      })


  }

  def main(args: Array[String]) {
    initGL(1400, 800);
    init();

    while (true) {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
      render();

      Display.update();
      Display.sync(100);

      if (Display.isCloseRequested()) {
        Display.destroy();
        System.exit(0);
      }
    }


  }
}

sealed class Texture(texFile: File) {

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


  private val start = System.currentTimeMillis()

  texBufferedImage = ImageIO.read(texFile)
  if (texBufferedImage == null) {
    throw new Exception("BufferedImage is null '" + texFile.getAbsoluteFile + "'")
  }

  format = FileFactory.getExtension(texFile)
  width = texBufferedImage.getWidth
  height = texBufferedImage.getHeight
  channels = texBufferedImage.getRaster.getNumBands
  imageSize = width * height
  imageDataSize = channels * imageSize



  imageData = texBufferedImage.getRaster.getDataBuffer.asInstanceOf[DataBufferByte].getData

  texBufferedImage.getType match {
    case BufferedImage.TYPE_3BYTE_BGR => {
      pixelFormat = GL12.GL_BGR //<-- most
      dataType = GL11.GL_UNSIGNED_BYTE
    }
    case BufferedImage.TYPE_INT_RGB => {
      pixelFormat = GL11.GL_RGB
      dataType = GL11.GL_INT
    }
    case BufferedImage.TYPE_4BYTE_ABGR => {
      pixelFormat = GL12.GL_BGRA // <-- some
      dataType = GL11.GL_UNSIGNED_BYTE
      imageData = reorderAlphaChannel(imageData, imageSize, channels)
    }
    case BufferedImage.TYPE_INT_ARGB => {
      pixelFormat = GL11.GL_RGBA
      dataType = GL11.GL_INT
      imageData = reorderAlphaChannel(imageData, imageSize, channels)
    }
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

  texId = glGenTextures()
  bind() //TODO: necessary?


  // Set parameters
  glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT)
  glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT)
  //Setup wrap mode
  /*glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE)
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE)*/

  glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST)
  glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)




  // Load the texture image
  glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, pixelFormat, dataType, imageBuffer)

  GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)

  private val end = System.currentTimeMillis()
  DC.log("Texture :" + texId + " '" + texFile.getName + "' loaded in " + (end - start) + "ms")


  /**
   * call this every time you want to use the texture
   */
  def bind() {
    glEnable(GL11.GL_TEXTURE_2D)
    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE)
    //GL13.glActiveTexture(GL13.GL_TEXTURE0)
    glBindTexture(GL_TEXTURE_2D, texId)
  }

  def unbind() {
    glDisable(GL11.GL_TEXTURE_2D)
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

