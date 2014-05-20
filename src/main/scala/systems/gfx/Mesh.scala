package main.scala.systems.gfx

import main.scala.math.{Mat4f, Vec3f}
import main.scala.tools.DC
import java.nio.{IntBuffer, FloatBuffer}
import org.lwjgl.BufferUtils


import scala.collection.mutable
import main.scala.io.Collada
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL12._
import org.lwjgl.opengl.GL13._
import org.lwjgl.opengl.GL14._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL21._
import org.lwjgl.opengl.GL30._
import org.lwjgl.opengl.GL31._
import org.lwjgl.opengl.GL32._
import org.lwjgl.opengl.GL33._
import org.lwjgl.opengl.GL40._
import org.lwjgl.opengl.GL41._
import org.lwjgl.opengl.GL42._
import org.lwjgl.opengl.GL43._
import org.lwjgl.opengl.GL44._


/**
 * Created by Christian Treffs
 * Date: 04.03.14 14:36
 */
object Mesh {
  def from(colladaFiles: Seq[Collada]): Seq[Mesh] = colladaFiles.flatMap(_.meshes)
  def from(colladaFile: Collada): Seq[Mesh] = from(Seq(colladaFile))

  var defaultShader: Shader = null
  private val meshes: mutable.HashMap[Symbol, Mesh] = mutable.HashMap.empty

  def getByName(name: Symbol): Mesh = meshes(name)

  def get(sym: Symbol): Mesh = {
    val seq = getByGroup(sym)
    if(seq.isEmpty) {
      val m = getByName(sym)
      if(m == null) {
        throw new IllegalArgumentException("can not find mesh "+sym)
      }
      m
    } else {
      seq.head
    }
  }

  def getByGroup(groupId: Symbol): Seq[Mesh] = meshes.values.filter(_.groupId==groupId).toSeq

  def defaultShader(shader: Shader) {
     defaultShader = shader
  }
}

trait MatrixFunctions[T] {
  def scale(x: Float, y: Float, z: Float): T
  def rotate(angle: Float, x: Float, y: Float, z: Float): T
  def translate(x: Float, y:Float, z:Float): T
  def mat(): T
}

class Mesh(gId: Symbol, iId: String, iName: String) extends MatrixFunctions[Mesh] {


  var groupId: Symbol = gId
  var id: Symbol = Symbol(iId)
  var name: Symbol = Symbol(iName)

  Mesh.meshes.put(name, this)

  final val verticesStride: Int = 3
  final var texCoordsStride: Int = 2
  final var positionsStride: Int = 3
  final var normalsStride: Int = 3

  var initialTransformation: Mat4f = Mat4f.identity
  var modelTransformation: Mat4f = Mat4f.identity

  var dataBuffer: FloatBuffer = null
  var dataBufferId: Int = -1

  var vertexOffset: Int = -1
  var normalOffset: Int = -1
  var texCoordOffset: Int = -1



  var positionsUnordered: Array[Float] = Array()
  var positionsArray: Array[Float] = Array()
  var positionsBuffer: FloatBuffer = null
  var hasPositions: Boolean = false

  var normalsUnordered: Array[Float] = Array()
  var normalsBuffer: FloatBuffer = null
  var normalsArray: Array[Float] = Array()
  var hasNormals: Boolean = false

  var texCoordsUnordered: Array[Float] = Array()
  var texCoordsArray: Array[Float] = Array()
  var texCoordsBuffer: FloatBuffer = null
  var hasTexCoords: Boolean = false

  var hasIndices: Boolean = false
  var hasVcount: Boolean = false
  var hasTexId: Boolean = false

  var isPolygons: Boolean = false
  var isPolyList: Boolean = false
  var isTriangles: Boolean = false

  var textureId: Int = -1

  var polytype: Int = -1

  //vcount = Die Anzahl der Vertices pro Polygon.
  var vcount: IntBuffer = null

  //P = Integer index über den Vertics
  var indexArray: Array[Int] = Array()

  var hasInitialTransformation = false


  def initRelativeTo(baseMesh: Mesh, position: Vec3f = null, scale: Vec3f = null, rotation: (Vec3f, Float) = null): Mesh = {
    var mat: Mat4f = baseMesh.modelTransformation
    if(position != null) {
      mat = Mat4f.translation(position)* mat
    }

    if(rotation != null) {
      val p = mat.getPosition
//      val t = Mat4f.translation(0,0,0) *mat
     // val f = Mat4f.rotation(rotation._1, rotation._2) * t
      mat = Mat4f.translation(p) * mat
    }


    if(scale != null) {
      mat =  mat * Mat4f.scale(scale)
    }


    // TODO keep initial trafo
    modelTransformation = mat
    this
  }

  //TODO: get scale from initial trafo -> INITIAL TRAFO!!!
  def init(position: Vec3f = null, scale: Vec3f = null, rotation: (Vec3f, Float) = null): Mesh = {
    var mat = Mat4f.identity
    if(position != null) {
      mat = mat * Mat4f.translation(position)
    }

    if(rotation != null) {
      mat = mat * Mat4f.rotation(rotation._1, rotation._2)
    }


    if(scale != null) {
      mat =  mat * Mat4f.scale(scale)
    }


    // TODO keep initial trafo
    modelTransformation = mat
    this
  }

  protected def bindTexture() {
    if(hasTexId) {
      Texture.bind(textureId)
    }
  }

  def setOffset(semantic: String, offset:Int) {
    semantic.toLowerCase match {
      case "vertex"   => vertexOffset = offset
      case "texcoord" => texCoordOffset = offset
      case "normal"   => normalOffset = offset
      case _ =>
    }
  }

  def setBuffers() {
    positionsArray = reorderArray(positionsUnordered, positionsStride, indexArray, vertexOffset, 3)
    positionsBuffer = BufferUtils.createFloatBuffer(positionsArray.length)
    positionsBuffer.put(positionsArray)
    positionsBuffer.flip()
    //positionsBuffer.rewind()

    normalsArray = reorderArray(normalsUnordered, normalsStride, indexArray, normalOffset, 3)
    normalsBuffer = BufferUtils.createFloatBuffer(normalsArray.length)
    normalsBuffer.put(normalsArray)
    normalsBuffer.flip()
    //normalsBuffer.rewind()

    texCoordsArray = reorderArray(texCoordsUnordered, texCoordsStride , indexArray, texCoordOffset, 3)
    texCoordsBuffer = BufferUtils.createFloatBuffer(texCoordsArray.length)
    texCoordsBuffer.put(texCoordsArray)
    texCoordsBuffer.flip()
    //texCoordsBuffer.rewind()


    /*
    dataBuffer = BufferUtils.createFloatBuffer(positionsArray.length+normalsArray.length+texCoordsArray.length)

    dataBuffer.put(positionsArray)
    dataBuffer.put(normalsArray)
    dataBuffer.put(texCoordsArray)

    dataBuffer.rewind()


    val dataBufferId = glGenVertexArrays()

    glBindVertexArray(dataBufferId)


    //dataBufferId = glGenBuffers
    glBindBuffer(GL_ARRAY_BUFFER, dataBufferId)
    glBufferData(GL_ARRAY_BUFFER, dataBuffer, GL_READ_ONLY)


    //glVertexAttribPointer(vertexAttributeIdx, 3, false, 0, dataBuffer)
    //glEnableVertexAttribArray(vertexAttributeIdx)

    println(dataBufferId)    */

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



  def draw(shader: Shader = Mesh.defaultShader, modelTransformation: Mat4f, viewMatrix: Mat4f, fov: Float,aspect:Float,zNear:Float,zFar:Float, beforeFunc: Unit => Unit = {Unit => Unit}, afterFunc: Unit => Unit = {Unit => Unit}) {

    //DC.log("Drawing at: \n" + modelTransformation.toString + " from \n" + viewMatrix.toString + "with \n" + projectionMatrix)
    beforeFunc()

    shader.useProgram(fov,aspect,zNear,zFar, viewMatrix)
    shader.setModelMatrix(modelTransformation)



    bindTexture()


    glVertexAttribPointer(shader.vertexAttributeIndex, 3, false, 0, positionsBuffer)
    glEnableVertexAttribArray(shader.vertexAttributeIndex)


    glVertexAttribPointer(shader.normalsAttributeIndex, 3, false, 0, normalsBuffer)
    glEnableVertexAttribArray(shader.normalsAttributeIndex)

    glVertexAttribPointer(shader.texCoordsAttributeIndex, 2, true, 0, texCoordsBuffer)
    glEnableVertexAttribArray(shader.texCoordsAttributeIndex)

    //TODO: correct limit
    glDrawArrays(polytype,0, texCoordsBuffer.limit/2)



    /*glDrawBuffer(shader.vertexAttributeIndex)
    glDrawBuffer(shader.normalsAttributeIndex)
    glDrawBuffer(shader.texCoordsAttributeIndex)

    println(positionsBuffer.limit(), normalsBuffer.limit(), texCoordsBuffer.limit())*/

    afterFunc()
  }

  def setInitialTransformation(mat: Mat4f): Mat4f = setInitialTransformation(mat.values)
  def setInitialTransformation(arr: Array[Float]): Mat4f = {
    initialTransformation = Mat4f(arr)
    hasInitialTransformation = true
    initialTransformation
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
    hasIndices = true
  }

  def setNormals(arr: Array[Float], stride: Int, count: Int) {
    normalsStride = stride
    normalsUnordered = arr
    if(arr.length != stride*count) {
      throw new IllegalArgumentException("normals count is wrong")
    }
    hasNormals = true
  }

  def setPositions(arr: Array[Float], stride: Int, count: Int) {
    positionsStride = stride
    positionsUnordered = arr
    if(arr.length != stride*count) {
      throw new IllegalArgumentException("positions count is wrong")
    }
    hasPositions = true
  }


  def setTexCoords(arr: Array[Float], stride: Int, count: Int) {
    texCoordsStride = stride
    texCoordsUnordered = arr
    if(arr.length != stride*count) {
      throw new IllegalArgumentException("tex coords count is wrong")
    }
    hasTexCoords = true
  }


  def setTexId(id: Int) {
    if(hasTexId) {
      throw new IllegalArgumentException("TexId already set "+this.name)
    }
    textureId = id
    if(textureId != -1) {
      hasTexId = true
    } else {
      DC.warn("No TexId was set ", name)
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
  override def mat(): Mesh = this

  override def translate(x: Float, y: Float, z: Float): Mesh = this

  override def rotate(angle: Float, x: Float, y: Float, z: Float): Mesh = this

  override def scale(x: Float, y: Float, z: Float): Mesh = this
}
