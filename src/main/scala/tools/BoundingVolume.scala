package main.scala.tools

import main.scala.systems.gfx.{Texture, Shader, DrawFunction}
import main.scala.math.{Mat4f, Vec3f}
import scala.collection.mutable
import java.nio.FloatBuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL11._
import main.scala.architecture.Component

/**
 * Created by Christian Treffs
 * Date: 23.05.14 02:01
 */
object BoundingVolumeType extends Enumeration {
  val AABB, Sphere = Value //axis-aligned bounding box (AABB)


}


object BoundingVolume {
  val t = Texture.load("src/main/resources/meshes/VolumeBoxTexture.png")
  val texID = t.texId
}

trait BoundingVolume extends DrawFunction {
  def centroid(): Vec3f

  def center(a: Float, b: Float): Float = (a+b)/2f

  def update(mat: Mat4f): Array[BBEndPoint]

  def owner: Component
  def setOwner(own: Component)



}
case class Sphere(cent: Vec3f, radius: Float) extends BoundingVolume{

  override def centroid(): Vec3f = cent

  override def update(mat: Mat4f): Array[BBEndPoint] = ???

  override def owner: Component = ???

  override def setOwner(own: Component): Unit = ???

  override def draw(shader: Shader, modelTransformation: Mat4f, viewMatrix: Mat4f, fov: Float, aspect: Option[Float], zNear: Float, zFar: Float, beforeFunc: (Unit) => Unit, afterFunc: (Unit) => Unit): Unit = ???
}

case class AABB(leftBottomBack: Vec3f, rightTopFront: Vec3f) extends BoundingVolume {

  private var _lbb: Vec3f = leftBottomBack
  private var _rtf: Vec3f = rightTopFront


  private var _owner: Component = null
  var endPoints: Array[BBEndPoint] = updateEndpoints(_lbb,_rtf)

  def updateEndpoints(lBB: Vec3f, rTF: Vec3f): Array[BBEndPoint] = {
    Array(
      new BBEndPoint(lBB.x(), 0, true)(this), //xMin
      new BBEndPoint(rTF.x(), 0, false)(this), //xMax
      new BBEndPoint(lBB.y(), 1, true)(this), //yMin
      new BBEndPoint(rTF.y(), 1, false)(this), //yMax
      new BBEndPoint(lBB.z(), 2, true)(this), //zMix
      new BBEndPoint(rTF.z(), 2, false)(this) //zMax
    )

  }

  def interval(axis: Int): Array[BBEndPoint] = {
    axis match {
      case 0 => Array(endPoints(0),endPoints(1)) //x
      case 1 => Array(endPoints(2),endPoints(3)) //y
      case 2 => Array(endPoints(4),endPoints(5)) //z
    }
  }


  override def update(mat: Mat4f): Array[BBEndPoint] = {

    //TODO: update to real position
    //println(mat.position.inline,leftBottomBack.inline,cent.inline,rightTopFront.inline)

    val centBefore = centroid()

    //println(mat.position.inline)

    val offset = mat.position-centBefore


    _lbb = _lbb+offset
    _rtf = _rtf+offset

    //println(offset.inline,_lbb.inline,_rtf.inline)

    endPoints = updateEndpoints(_lbb,_rtf)


    //endPoints.foreach(_.update(atPosition))
    //println(endPoints.toList)
    endPoints
  }

  override def centroid(): Vec3f = Vec3f(center(endPoints(0).value,endPoints(1).value),center(endPoints(2).value,endPoints(3).value),center(endPoints(4).value,endPoints(5).value))

  override def owner: Component = _owner

  override def setOwner(own: Component): Unit = {
    _owner = own
  }

  var posArray: mutable.ArrayBuffer[Float] = mutable.ArrayBuffer.empty[Float]
  var texArray: mutable.ArrayBuffer[Float] = mutable.ArrayBuffer.empty[Float]
  var positionsBuffer: FloatBuffer = null
  var normalsBuffer: FloatBuffer = null
  var texCoordsBuffer: FloatBuffer = null

  override def draw(shader: Shader, modelTransformation: Mat4f, viewMatrix: Mat4f, fov: Float, aspect: Option[Float], zNear: Float, zFar: Float, beforeFunc: (Unit) => Unit, afterFunc: (Unit) => Unit): Unit = {

    beforeFunc()

    //    shader.useProgram(projectionMatrix, viewMatrix)
    //shader.setModelMatrix(modelTransformation)

    var posArray: mutable.ArrayBuffer[Float] = mutable.ArrayBuffer.empty[Float]
    var texArray: mutable.ArrayBuffer[Float] = mutable.ArrayBuffer.empty[Float]
    var positionsBuffer: FloatBuffer = null
    var texCoordsBuffer: FloatBuffer = null


    shader.useProgram(fov,aspect,zNear,zFar, viewMatrix)
    shader.setModelMatrix(modelTransformation)

    //glPushAttrib(GL_TEXTURE_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT)
    //glEnable(GL_CULL_FACE)

    Texture.bind(BoundingVolume.texID)

    // back
    texArray += 0
    texArray += 0
    posArray += _lbb.x
    posArray += _lbb.y
    posArray += _lbb.z

    texArray += 0
    texArray += 1
    posArray += _rtf.x
    posArray += _lbb.y
    posArray += _lbb.z

    texArray += 1
    texArray += 1
    posArray += _rtf.x
    posArray += _rtf.y
    posArray += _lbb.z

    texArray += 1
    texArray += 0
    posArray += _lbb.x
    posArray += _rtf.y
    posArray += _lbb.z

    //right
    texArray += 0
    texArray += 0
    posArray += _rtf.x
    posArray += _lbb.y
    posArray += _lbb.z

    texArray += 0
    texArray += 1
    posArray += _rtf.x
    posArray += _lbb.y
    posArray += _rtf.z

    texArray += 1
    texArray += 1
    posArray += _rtf.x
    posArray += _rtf.y
    posArray += _rtf.z

    texArray += 1
    texArray += 0
    posArray += _rtf.x
    posArray += _rtf.y
    posArray += _lbb.z

    //left
    texArray += 0
    texArray += 0
    posArray += _lbb.x
    posArray += _lbb.y
    posArray += _rtf.z

    texArray += 0
    texArray += 1
    posArray += _lbb.x
    posArray += _lbb.y
    posArray += _lbb.z

    texArray += 1
    texArray += 1
    posArray += _lbb.x
    posArray += _rtf.y
    posArray += _lbb.z

    texArray += 1
    texArray += 0
    posArray += _lbb.x
    posArray += _rtf.y
    posArray += _rtf.z


    //front
    texArray += 0
    texArray += 0
    posArray += _rtf.x
    posArray += _lbb.y
    posArray += _rtf.z

    texArray += 0
    texArray += 1
    posArray += _lbb.x
    posArray += _lbb.y
    posArray += _rtf.z

    texArray += 1
    texArray += 1
    posArray += _lbb.x
    posArray += _rtf.y
    posArray += _rtf.z

    texArray += 1
    texArray += 0
    posArray += _rtf.x
    posArray += _rtf.y
    posArray += _rtf.z


    positionsBuffer = BufferUtils.createFloatBuffer(posArray.length)
    texCoordsBuffer = BufferUtils.createFloatBuffer(texArray.length)

    positionsBuffer.put(posArray.toArray)
    positionsBuffer.flip()

    texCoordsBuffer.put(texArray.toArray)
    texCoordsBuffer.flip()

    glVertexAttribPointer(shader.vertexAttributeIndex, 3, false, 0, positionsBuffer)
    glEnableVertexAttribArray(shader.vertexAttributeIndex)



    glVertexAttribPointer(shader.texCoordsAttributeIndex, 2, true, 0, texCoordsBuffer)
    glEnableVertexAttribArray(shader.texCoordsAttributeIndex)

    //TODO: correct limit
    glDrawArrays(GL_QUADS,0, texCoordsBuffer.limit/2)


    afterFunc()


  }
}


case class BBEndPoint(var value: Float, axis: Int, isMin: Boolean)(implicit sub: BoundingVolume) {
  //println("BBEndPoint",value,axis,isMin)

  def update(v: Float) {value = value }//+ v} //TODO: update position & rotation and everything else
  def update(vec: Vec3f) {
    //println("update a with ",axis,vec.inline)
    axis match {
      case 0 => update(vec.x)
      case 1 => update(vec.y)
      case 2 => update(vec.z)
    }
  }

  def owner(): Component = sub.owner

  override def toString: String = {
    if(isMin) ""+value+" min"
    else  ""+value+" max"
  }
}
