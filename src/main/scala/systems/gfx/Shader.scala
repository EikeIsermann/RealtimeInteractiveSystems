package main.scala.systems.gfx

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import main.scala.io.FileIO


import java.nio.ByteBuffer
import ogl.app.{MatrixUniform, Util}
import main.scala.math.Mat4f
import org.lwjgl.BufferUtils
import main.scala.tools.DC
import scala.collection.mutable
import scala.xml.{XML, Elem}

/**
 * Created by Christian Treffs
 * Date: 12.03.14 13:50
 */
object Shader {

  final val defaultVertexShader = "src/main/resources/shaders/default.vs"
  final val defaultFragmentShader = "src/main/resources/shaders/default.fs"

  private val shaderMap = mutable.HashMap.empty[Symbol, Shader]

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
  
  def get(identifier: Symbol): Shader = shaderMap(identifier)
  
  def load(shaderDir: String = "src/main/resources/shaders/", definitionsFile: String = "shaderDefinitions.xml") = {
    parseXML(XML.loadFile(FileIO.load(shaderDir+"/"+definitionsFile)), shaderDir)
    DC.log("Shader created",shaderMap.values.size, 2)
    shaderMap
  }

  private def parseXML(xml: Elem,shaderDir:String) {
    val shaders = xml \\ "shader"
    shaders.foreach(s =>{
        val id = Symbol(s \ "@id" text)
        val vsSrc = shaderDir+"/"+(s \ "vsSource").text
        val fsSrc = shaderDir+"/"+(s \ "fsSource").text
        val shader = Shader.init(vsSrc,fsSrc)
        shaderMap.put(id, shader)
    })
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
    DC.log("SET MODEL MATRIX TO",mat.getPosition)
    mat
  }

  protected def setProperties() {
    glShadeModel  (GL_SMOOTH);			// Enables Smooth Color Shading
    glEnable      (GL_DEPTH_TEST)	// Enables Depth Testing
    glEnable      (GL_BLEND) // enable alpha blending
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
    DC.log("Shader","setProperties")
  }

  protected def createProgram()  {
    prog = glCreateProgram()
    DC.log("Shader","createProgram")
  }

  protected def setVertexShader(vsFilePath: String) {
    val arr = FileIO.loadAsArray[Byte](vsFilePath)
    val vertexShader: ByteBuffer = BufferUtils.createByteBuffer(arr.length)
    vertexShader.put(arr)
    vertexShader.flip()

    vsIdx = glCreateShader(GL_VERTEX_SHADER)
    glShaderSource(vsIdx, vertexShader)
    glCompileShader(vsIdx)
    Util.checkCompilation(vsIdx)

    glAttachShader(programIndex(), vsIdx)
    DC.log("Shader","setVertexShader")
  }

  protected def setFragmentShader(fsFilePath: String) {
    val arr = FileIO.loadAsArray[Byte](fsFilePath)

    val fragmentShader: ByteBuffer = BufferUtils.createByteBuffer(arr.length)
    fragmentShader.put(arr)
    fragmentShader.flip()

    fsIdx = glCreateShader(GL_FRAGMENT_SHADER)
    glShaderSource(fsIdx, fragmentShader)
    glCompileShader(fsIdx)
    Util.checkCompilation(fsIdx)

    glAttachShader(programIndex(), fsIdx)
    DC.log("Shader","setFragmentShader")
  }

  protected def bindAttribLocations() {
    // Bind the vertex attribute data locations for this shader program. The
    // shader expects to get vertex and color data from the mesh. This needs to
    // be done *before* linking the program.
    glBindAttribLocation(programIndex(), vertexAttributeIndex, "vertex")
    glBindAttribLocation(programIndex(), normalsAttributeIndex, "normal")
    glBindAttribLocation(programIndex(), texCoordsAttributeIndex, "texCoord")
    DC.log("Shader","bindAttribLocations")
  }

  protected def linkProgram() {
    // Link the shader program.
    glLinkProgram(programIndex())
    Util.checkLinkage(programIndex())
    DC.log("Shader","linkProgram")
  }

  protected def bindMatrixUniforms() {
    // Bind the matrix uniforms to locations on this shader program. This needs
    // to be done *after* linking the program.
    projectionMatrix = new MatrixUniform(programIndex(), "projectionMatrix")
    viewMatrix = new MatrixUniform(programIndex(), "viewMatrix")
    modelMatrix = new MatrixUniform(programIndex(), "modelMatrix")
    DC.log("Shader","bindMatrixUniforms")
  }

  /**
   * use the shader
   */
  def useProgram(projection: Mat4f = defaultProjection, view: Mat4f = defaultView) {
    glUseProgram(programIndex())

    projectionMatrix.set(projection)
    viewMatrix.set(view)

    DC.log("USE PROGRAM", ("View Mat", view.getPosition))
  }
}
