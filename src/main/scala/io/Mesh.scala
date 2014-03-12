package main.scala.io

import main.scala.math.Vec3f
import main.scala.tools.DC
import java.nio.{IntBuffer, FloatBuffer}
import org.lwjgl.BufferUtils

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import ogl.app.MatrixUniform
import ogl.vecmathimp.FactoryDefault
import scala.collection.mutable


/**
 * Created by Christian Treffs
 * Date: 04.03.14 14:36
 */
object Mesh {
  def from(colladaFiles: Seq[Collada]): Seq[Mesh] = colladaFiles.flatMap(_.meshes)
  def from(colladaFile: Collada): Seq[Mesh] = from(Seq(colladaFile))

  private val meshes: mutable.HashMap[Symbol, Mesh] = mutable.HashMap.empty

  def get(name: Symbol): Mesh = meshes(name)
  def getGroup(groupId: Symbol): Seq[Mesh] = meshes.values.filter(_.groupId==groupId).toSeq
}

case class Mesh(groupId: Symbol, initialId: String, initialName: String) {

  Mesh.meshes.put(Symbol(initialName), this)

  final val verticesStride: Int = 3
  final var texCoordsStride: Int = 2
  final var positionsStride: Int = 3
  final var normalsStride: Int = 3

  var initialTransformation: Array[Float] = Array()





  var positionsUnordered: Array[Vec3f] = Array()
  var positionsArray: Array[Float] = Array()
  var positionsBuffer: FloatBuffer = null
  var hasPositions: Boolean = false

  var positions: Array[Vec3f] = Array()
  var normals: Array[Vec3f] = Array()
  var texCoords: Array[Vec3f] = Array()
  
  var normalsUnordered: Array[Vec3f] = Array()
  var normalsBuffer: FloatBuffer = null
  var normalsArray: Array[Float] = Array()
  var hasNormals: Boolean = false
  var hasIndices: Boolean = false
  var hasVcount: Boolean = false
  var hasTexId: Boolean = false

  var texCoordsUnordered: Array[Vec3f] = Array()
  var texCoordsArray: Array[Float] = Array()
  var texCoordsBuffer: FloatBuffer = null
  var hasTexCoords: Boolean = false

  var isPolygons: Boolean = false
  var isPolyList: Boolean = false
  var isTriangles: Boolean = false

  var newPosIdx: Array[Int] = Array()
  var newNormIdx: Array[Int] = Array()
  var newTexIdx: Array[Int] = Array()

  var iPos: IntBuffer = null

  var textureId: Int = -1

  var polytype: Int = -1

  //vcount = Die Anzahl der Vertics pro Polygon.
  var vcount: IntBuffer = null

  //P = Integer index über den Vertics
  var indexBuffer: IntBuffer = null
  var indexArray: Array[Int] = Array()

  var hasInitialTransformation = false


  protected def bindTexture() {
    if(hasTexId) {
      Texture.bind(textureId)
    }
  }


  def scale(x: Float, y: Float, z: Float): Mesh = {
        //TODO:
    this
  }
  def rotate(angle: Float, x: Float, y: Float, z: Float): Mesh = {
    //TODO:
    this
  }

  def translate(x: Float, y:Float, z:Float): Mesh = {
        //TODO
    this
  }

  def init() {
    //calcIndices()

    val pArr = reorderArray(positionsArray, 3, indexArray, 0, 3)

    positionsBuffer = BufferUtils.createFloatBuffer(pArr.length)
    positionsBuffer.put(pArr)
    positionsBuffer.rewind()

    val nArr = reorderArray(normalsArray, 3, indexArray, 1, 3)

    normalsBuffer = BufferUtils.createFloatBuffer(nArr.length)
    normalsBuffer.put(nArr)
    normalsBuffer.rewind()

    val texArr = reorderArray(texCoordsArray, 2 , indexArray, 2, 3)
    //val texArr = texCoordsArray
    texCoordsBuffer = BufferUtils.createFloatBuffer(texArr.length)
    texCoordsBuffer.put(texArr)
    texCoordsBuffer.rewind()

    println("Buffers created")

  }



  def reorderArray(arr: Array[Float],  stride: Int, indexArr: Array[Int], indexOffset: Int,indexStride: Int = 3): Array[Float] = {

    val temp = new Array[Float](indexArr.length/indexStride*stride)

    for(i <- indexOffset until indexArr.length by indexStride)  {
      val id = indexArr(i)*stride
      for(j <- 0 until stride) {
        temp((i-indexOffset)/indexStride*stride+j)   = arr(id+j)
      }
    }
    temp
  }

  /**
   * draw the mesh and texture it
   */

  def draw(program: Int, vertexAttributeIdx: Int, normalsAttributeIdx: Int, texCoordAttributeIdx: Int, beforeFunc: Unit => Unit = {Unit => Unit}, afterFunc: Unit => Unit = {Unit => Unit}) {

    beforeFunc()

    // use program
    glUseProgram(program)


    // Bind the matrix uniforms to locations on this shader program. This needs
    // to be done *after* linking the program.
    //TODO: here? -> give shader
    val modelMatrixUniform = new MatrixUniform(program, "modelMatrix")
    val viewMatrixUniform = new MatrixUniform(program, "viewMatrix")
    val projectionMatrixUniform = new MatrixUniform(program, "projectionMatrix")

    viewMatrixUniform.set(FactoryDefault.vecmath.identityMatrix())
    projectionMatrixUniform.set(FactoryDefault.vecmath.perspectiveMatrix(70f, 1, 0.1f, 20f ))

    //modelMatrixUniform.set(FactoryDefault.vecmath.translationMatrix(0,0,-5f))
    //val mat = FactoryDefault.vecmath.matrix(initialTransformation).mult(FactoryDefault.vecmath.translationMatrix(0,0.05f,0f))
    val scale = FactoryDefault.vecmath.scaleMatrix(0.01f,0.01f,0.01f)
    val rot = FactoryDefault.vecmath.rotationMatrix(Vec3f(0,1,0), 90)
    modelMatrixUniform.set(FactoryDefault.vecmath.translationMatrix(0,0f,-8).mult(scale).mult(rot))

    //println(mat.getPosition)



    bindTexture()

    glVertexAttribPointer(vertexAttributeIdx, 3, false, 0, positionsBuffer)
    glEnableVertexAttribArray(vertexAttributeIdx)

    glVertexAttribPointer(normalsAttributeIdx, 3, false, 0, normalsBuffer)
    glEnableVertexAttribArray(normalsAttributeIdx)

    glVertexAttribPointer(texCoordAttributeIdx, 2, false, 0, texCoordsBuffer)
    glEnableVertexAttribArray(texCoordAttributeIdx)


    //TODO: vertices count

    glDrawArrays(polytype,0, positionsBuffer.limit()/3)

    afterFunc()
  }

  def setInitialTransformation(arr: Array[Float]) {
    initialTransformation = arr
    hasInitialTransformation = true
  }

  def setVcount(arr: Array[Int], material: String) {
    vcount = BufferUtils.createIntBuffer(arr.length)
    vcount.put(arr)
    vcount.rewind()


    if(arr.forall(_ == arr(0))) {
      setPolygonMode(arr(0))
    } else {
      throw new IllegalArgumentException("Mixed Polytypes not supported")
    }

    hasVcount = true
  }


  def setPolygonMode(nr: Int) {
    nr match {
      case 3 => polytype = GL_TRIANGLES
      case 4 => polytype = GL_QUADS
      case _ => polytype = GL_POLYGON
    }

  }

  def setIndex(arr: Array[Int], material: String) {
    indexArray = arr

    indexBuffer = BufferUtils.createIntBuffer(arr.length)
    indexBuffer.put(arr)
    indexBuffer.rewind()
    hasIndices = true
  }

  def setNormals(arr: Array[Float], stride: Int, strideCount: Int) {
    normalsStride = stride
    normalsArray = arr
    hasNormals = true
  }

  def setPositions(arr: Array[Float], stride: Int, strideCount: Int) {
    positionsStride = stride
    positionsArray = arr
    hasPositions = true
  }


  def setTexCoords(arr: Array[Float], stride: Int, strideCount: Int) {
    texCoordsStride = stride
    texCoordsArray = arr
    hasTexCoords = true
  }


  def setTexId(id: Int) {
    if(hasTexId) {
      throw new IllegalArgumentException("TexId already set "+this.initialName)
    }
    textureId = id
    if(textureId != -1) {
      hasTexId = true
    } else {
      DC.warn("No TexId was set ", initialName)
    }
  }

  //NOT USED ANYMORE

  /*
  private def parseToVec2fArray(floatArr: Array[Float], newLength: Int, stride: Int = 2): Array[Vec3f] = {


    val ret = new Array[Vec3f](newLength)
    for(i <- 0 until floatArr.length by stride) {
      ret(i/stride) = new Vec3f(floatArr(i), floatArr(i+1), 0)
    }
    ret
  }


  private def parseToVec3fArray(floatArr: Array[Float], newLength: Int, stride: Int = 3): Array[Vec3f] = {
    val ret = new Array[Vec3f](newLength)
    for(i <- 0 until floatArr.length by stride) {
      ret(i/stride) = Vec3f(floatArr(i), floatArr(i+1), floatArr(i+2))
    }
    ret
  }*/




  //Vertex attributes
  //• Normal vector.
  //• Texture coordinates
   /*
  def calcIndices() {

    newPosIdx = new Array[Int](indexArray.length/3)
    newNormIdx = new Array[Int](indexArray.length/3)
    newTexIdx = new Array[Int](indexArray.length/3)

    var count = 0
    for (c <- 0 until indexArray.length by 3) {
      newPosIdx(count) = indexArray(c+vertexOffset)
      newNormIdx(count) = indexArray(c+normalOffset)
      newTexIdx(count) = indexArray(c+texCoordOffset)

      count = count + 1

    }

    iPos = BufferUtils.createIntBuffer(newPosIdx.length)
    iPos.put(newPosIdx)
    iPos.rewind()
  }
  */

  /*def reorderData() {

    val newPosIdx: Array[Int] = new Array[Int](indexArray.length/3)
    val newNormIdx: Array[Int] = new Array[Int](indexArray.length/3)
    val newTexIdx: Array[Int] = new Array[Int](indexArray.length/3)

    var count = 0
    for (c <- 0 until indexArray.length by 3) {
      newPosIdx(count) = indexArray(c+vertexOffset)
      newNormIdx(count) = indexArray(c+normalOffset)
      newTexIdx(count) = indexArray(c+texCoordOffset)

      count = count + 1

    }


    positions = new Array[Vec3f](newPosIdx.length)
    normals = new Array[Vec3f](newNormIdx.length)
    texCoords = new Array[Vec3f](newTexIdx.length)



    for(i <- 0 until newPosIdx.length) {
      positions(i) = positionsUnordered(newPosIdx(i))
      normals(i) = normalsUnordered(newNormIdx(i))
      texCoords(i) = texCoordsUnordered(newTexIdx(i))
    }

  }*/
}
