package main.scala.shader

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import main.scala.io.File


import java.nio.ByteBuffer
import ogl.app.{MatrixUniform, Util}
import com.ardor3d.util.geom.BufferUtils
import main.scala.math.Mat4f

/**
 * Created by Christian Treffs
 * Date: 12.03.14 13:50
 */
object Shader {

  final val defaultVertexShader = "src/main/scala/shader/default.vs"
  final val defaultFragmentShader = "src/main/scala/shader/default.fs"


  def init(vs: String = defaultVertexShader, fs: String = defaultFragmentShader): Shader = {
    val shader = new Shader
    shader.createProgram()
    shader.setVertexShader(vs)
    shader.setFragmentShader(fs)
    shader.bindAttribLocations()
    shader.linkProgram()
    shader.bindMatrixUniforms()
    shader.setProperties()
    shader
  }

}

sealed class Shader() {
  var modelMatrix: MatrixUniform = null
  var viewMatrix: MatrixUniform = null
  var projectionMatrix: MatrixUniform = null

  final val vertexAttributeIndex = 0
  final val normalsAttributeIndex = 1
  final val texCoordsAttributeIndex = 2

  final val defaultProjection: Mat4f = Mat4f.projection(70f, 1, 0.1f, 20f )
  final val defaultView: Mat4f = Mat4f.identity

  private var prog: Int = -1
  private var vsIdx: Int = -1
  private var fsIdx: Int = -1


  def vsIndex() = vsIdx
  def fsIndex() = fsIdx
  def programIndex() = prog


  /**
   * transform the model matrix with this matrix
   * @param mat the matrix
   * @return the shader
   */
  def setModelMatrix(mat: Mat4f): Mat4f = {
    modelMatrix.set(mat)
    mat
  }

  protected def setProperties() {
    glShadeModel  (GL_SMOOTH);			// Enables Smooth Color Shading
    glEnable      (GL_DEPTH_TEST)	// Enables Depth Testing
    glEnable      (GL_BLEND) // enable alpha blending
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
  }

  protected def createProgram()  {
    prog = glCreateProgram()
  }

  protected def setVertexShader(vsFilePath: String) {
    val arr = File.loadAsArray[Byte](vsFilePath)
    val vertexShader: ByteBuffer = BufferUtils.createByteBuffer(arr.length)
    vertexShader.put(arr)
    vertexShader.rewind()

    vsIdx = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vsIdx, vertexShader)
    glCompileShader(vsIdx)
    Util.checkCompilation(vsIdx)

    glAttachShader(programIndex(), vsIdx)
  }

  protected def setFragmentShader(fsFilePath: String) {
    val arr = File.loadAsArray[Byte](fsFilePath)

    val fragmentShader: ByteBuffer = BufferUtils.createByteBuffer(arr.length)
    fragmentShader.put(arr)
    fragmentShader.rewind()

    fsIdx = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fsIdx, fragmentShader)
    glCompileShader(fsIdx)
    Util.checkCompilation(fsIdx)

    glAttachShader(programIndex(), fsIdx)
  }

  protected def bindAttribLocations() {
    // Bind the vertex attribute data locations for this shader program. The
    // shader expects to get vertex and color data from the mesh. This needs to
    // be done *before* linking the program.
    glBindAttribLocation(programIndex(), vertexAttributeIndex, "vertex")
    glBindAttribLocation(programIndex(), normalsAttributeIndex, "normal")
    glBindAttribLocation(programIndex(), texCoordsAttributeIndex, "texCoord")
  }

  protected def linkProgram() {
    // Link the shader program.
    glLinkProgram(programIndex())
    Util.checkLinkage(programIndex())
  }

  protected def bindMatrixUniforms() {
    // Bind the matrix uniforms to locations on this shader program. This needs
    // to be done *after* linking the program.
    projectionMatrix = new MatrixUniform(programIndex(), "projectionMatrix")
    viewMatrix = new MatrixUniform(programIndex(), "viewMatrix")
    modelMatrix = new MatrixUniform(programIndex(), "modelMatrix")
  }

  /**
   * use the shader
   */
  def useProgram(projection: Mat4f = defaultProjection, view: Mat4f = defaultView) {
    glUseProgram(programIndex())
    projectionMatrix.set(projection)
    viewMatrix.set(view)
  }
}
