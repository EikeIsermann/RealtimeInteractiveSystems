package main.scala.io

import main.scala.math.Vec3f
import main.scala.tools.DC
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 04.03.14 14:36
 */
case class Mesh(groupId: String, initialId: String, initialName: String) {
  final val verticesStride: Int = 3
  final val texCoordsStride: Int = 2
  final val positionsStride: Int = 3
  final val normalsStride: Int = 3

  var initialTransformation: Array[Float] = Array()

  var vertices: Array[Vec3f] = Array()
  var verticesArray: Array[Float] = Array()
  var hasVertices: Boolean = false

  var positions: Array[Vec3f] = Array()
  var positionsArray: Array[Float] = Array()
  var hasPositions: Boolean = false

  var normals: Array[Vec3f] = Array()
  var normalsArray: Array[Float] = Array()
  var hasNormals: Boolean = false
  var hasP: Boolean = false
  var hasVcount: Boolean = false

  var texCoords: Array[Vec3f] = Array()
  var texCoordsArray: Array[Float] = Array()
  var hasTexCoords: Boolean = false

  var isPolygons: Boolean = false
  var isPolyList: Boolean = false
  var isTriangles: Boolean = false


  //vcount = Die Anzahl der Vertics pro Polygon.
  val vcount= mutable.HashMap.empty[String, Array[Int]]

  //P = Integer index über den Vertics
  val p = mutable.HashMap.empty[String, Array[Int]]

  var hasInitialTransformation = false




  def setInitialTransformation(arr: Array[Float]) {
    initialTransformation = arr
    hasInitialTransformation = true
  }

  def setVcount(arr: Array[Int], material: String) {
    vcount.put(material, arr)
    hasVcount = true
  }

  def setP(arr: Array[Int], material: String) {
    p.put(material, arr)
    hasP = true
  }

  def setNormals(arr: Array[Float], stride: Int, strideCount: Int) {
    if(stride != normalsStride) {
      throw new IllegalArgumentException("normals stride not compatible for "+initialName)
    }
    normalsArray = arr
    normals = parseToVec3fArray(normalsArray)
    if(normals.length != strideCount)   {
      throw new IllegalArgumentException("normals count not correct @"+initialName)
    }
    hasNormals = true
  }

  def setVertices(arr: Array[Float], stride: Int, strideCount: Int) {
    if(stride != verticesStride) {
      throw new IllegalArgumentException("vertices stride not compatible for "+initialName)
    }
    verticesArray = arr
    vertices = parseToVec3fArray(verticesArray)
    if(vertices.length != strideCount)   {
      throw new IllegalArgumentException("vertices count not correct @"+initialName)
    }
    hasVertices = true
  }

  def setPositions(arr: Array[Float], stride: Int, strideCount: Int) {
    if(stride != positionsStride) {
      throw new IllegalArgumentException("positions stride not compatible for "+initialName)
    }
    positionsArray = arr
    positions = parseToVec3fArray(positionsArray)
    if(positions.length != strideCount)   {
      throw new IllegalArgumentException("positions count not correct @"+initialName)
    }
    hasPositions = true
  }


  def setTexCoords(arr: Array[Float], stride: Int, strideCount: Int) {
    if(stride != texCoordsStride) {
      throw new IllegalArgumentException("texCoords stride not compatible for "+initialName)
    }
    texCoordsArray = arr
    texCoords = parseToVec2fArray(texCoordsArray)
    if(texCoords.length != strideCount)   {
      throw new IllegalArgumentException("texCoords count not correct @"+initialName)
    }
    hasTexCoords = true
  }


  private def parseToVec2fArray(floatArr: Array[Float]): Array[Vec3f] = {
    //TODO: real Vec2f
    val length = floatArr.length/2
    val ret = new Array[Vec3f](length)
    for(i <- 0 to length-1 by 3) {
      ret(i) = Vec3f(floatArr(i), floatArr(i+1), 0)
    }
    ret
  }


  private def parseToVec3fArray(floatArr: Array[Float]): Array[Vec3f] = {
    val length = floatArr.length/3
    val ret = new Array[Vec3f](length)
    for(i <- 0 to length-1 by 3) {
      ret(i) = Vec3f(floatArr(i), floatArr(i+1), floatArr(i+2))
    }
    ret
  }


  //Vertex attributes
  //• Normal vector.
  //• Texture coordinates
}
