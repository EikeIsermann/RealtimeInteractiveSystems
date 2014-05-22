package main.scala.systems.gfx

import org.lwjgl.opengl.GL11._
import main.scala.math.Mat4f
import main.scala.tools.DC
import org.lwjgl.opengl.GL20._
import java.nio.FloatBuffer
import org.lwjgl.BufferUtils
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 24.03.14 15:49
 */
class Font extends DrawFunction {
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

  var stringToDraw: String = ""

  override def draw(shader: Shader, modelTransformation: Mat4f, viewMatrix: Mat4f, fov: Float, aspect: Option[Float], zNear: Float, zFar: Float, beforeFunc: (Unit) => Unit, afterFunc: (Unit) => Unit): Unit = {
    beforeFunc()

    //    shader.useProgram(projectionMatrix, viewMatrix)
    //shader.setModelMatrix(modelTransformation)

    var posArray: mutable.ArrayBuffer[Float] = ArrayBuffer.empty[Float]
    var texArray: mutable.ArrayBuffer[Float] = ArrayBuffer.empty[Float]
    var positionsBuffer: FloatBuffer = null
    var normalsBuffer: FloatBuffer = null
    var texCoordsBuffer: FloatBuffer = null


    shader.useProgram(fov,aspect,zNear,zFar, viewMatrix)
    shader.setModelMatrix(modelTransformation)

    glPushAttrib(GL_TEXTURE_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT)
    glEnable(GL_CULL_FACE)

    Texture.bind(fontTextureId)

    //glBegin(GL_QUADS)
    for (i <- 0 until stringToDraw.length) {
      val y = 0

      val a: Int = stringToDraw.charAt(i)

      //glTexCoord2f(tX(a), tY(a) + cellSize)
      texArray += tX(a)
      texArray += tY(a) + cellSize
      //glVertex3f(i * characterWidth / 3, y,0)
      posArray += i * characterWidth / 3
      posArray += y
      posArray += 0

      //glTexCoord2f(tX(a) + cellSize, tY(a) + cellSize)
      texArray += tX(a) + cellSize
      texArray += tY(a) + cellSize
      //glVertex3f(i * characterWidth / 3 + characterWidth / 2, y,0)
      posArray += i * characterWidth / 3 + characterWidth / 2
      posArray += y
      posArray += 0

      //glTexCoord2f(tX(a) + cellSize, tY(a))
      texArray += tX(a) + cellSize
      texArray += tY(a)
      //glVertex3f(i * characterWidth / 3 + characterWidth / 2, y + characterHeight,0)
      posArray += i * characterWidth / 3 + characterWidth / 2
      posArray += y + characterHeight
      posArray += 0

      //glTexCoord2f(tX(a), tY(a))
      texArray += tX(a)
      texArray += tY(a)
      //glVertex3f(i * characterWidth / 3, y + characterHeight,0)
      posArray += i * characterWidth / 3
      posArray +=  y + characterHeight
      posArray +=  0
    }
    //glEnd()




    positionsBuffer = BufferUtils.createFloatBuffer(posArray.length)
    texCoordsBuffer = BufferUtils.createFloatBuffer(texArray.length)

    positionsBuffer.put(posArray.toArray)
    positionsBuffer.flip()

    texCoordsBuffer.put(texArray.toArray)
    texCoordsBuffer.flip()

    glVertexAttribPointer(shader.vertexAttributeIndex, 3, false, 0, positionsBuffer)
    glEnableVertexAttribArray(shader.vertexAttributeIndex)


    //glVertexAttribPointer(shader.normalsAttributeIndex, 3, false, 0, normalsBuffer)
    //glEnableVertexAttribArray(shader.normalsAttributeIndex)

    glVertexAttribPointer(shader.texCoordsAttributeIndex, 2, true, 0, texCoordsBuffer)
    glEnableVertexAttribArray(shader.texCoordsAttributeIndex)

    //TODO: correct limit
    glDrawArrays(GL_QUADS,0, texCoordsBuffer.limit/2)


    afterFunc()
  }


  def deinit() {

  }


}
