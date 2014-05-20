package main.scala.systems.gfx

import org.lwjgl.opengl.GL11._
import main.scala.math.Mat4f

/**
 * Created by Christian Treffs
 * Date: 24.03.14 15:49
 */
object Font {
  private val defaultFontImageFilePath = "src/main/resources/fonts/Arial@54@1024x@alpha.png"

  private var characterWidth: Float = -1
  private var characterHeight: Float = -1
  private var fontTextureId: Int = -1
  private var cellSize: Float = -1

  private var tX: Array[Float] = null
  private var tY: Array[Float] = null

  //http://www.webspaceworks.com/resources/fonts-web-typography/43/
  def init(filePath: String = defaultFontImageFilePath, gridSize: Int = 16, aspect: Float = 0.52f, fontSize: Float = 0.3f) {

    characterWidth = fontSize
    characterHeight = aspect * characterWidth

    fontTextureId = Texture.load(filePath, flipped = false).texId
    cellSize = 1.0f/gridSize.toFloat

    tX = new Array[Float](gridSize*gridSize)
    tY = new Array[Float](gridSize*gridSize)

    for(i <- 0 until tX.length) {
      tX(i) = ( i % gridSize) * cellSize
    }
    for(j <- 0 until tY.length) {
      tY(j) =  (j / gridSize) * cellSize
    }

  }


  def draw(string: String, modelTransformation: Mat4f, projectionMatrix: Mat4f, viewMatrix: Mat4f, shader: Shader = Shader.init(),beforeFunc: Unit => Unit = {Unit => Unit}, afterFunc: Unit => Unit = {Unit => Unit}) {

    beforeFunc()

//    shader.useProgram(projectionMatrix, viewMatrix)
    shader.setModelMatrix(modelTransformation)

    glPushAttrib(GL_TEXTURE_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT)
    glEnable(GL_CULL_FACE)

    Texture.bind(fontTextureId)

    glBegin(GL_QUADS)
    for (i <- 0 until string.length) {
      val y = 0

      val a: Int = string.charAt(i)

      glTexCoord2f(tX(a), tY(a) + cellSize)
      glVertex2f(i * characterWidth / 3, y)

      glTexCoord2f(tX(a) + cellSize, tY(a) + cellSize)
      glVertex2f(i * characterWidth / 3 + characterWidth / 2, y)

      glTexCoord2f(tX(a) + cellSize, tY(a))
      glVertex2f(i * characterWidth / 3 + characterWidth / 2, y + characterHeight)

      glTexCoord2f(tX(a), tY(a))
      glVertex2f(i * characterWidth / 3, y + characterHeight)

    }
    glEnd()

    afterFunc()
  }




  def deinit() {

  }


}
