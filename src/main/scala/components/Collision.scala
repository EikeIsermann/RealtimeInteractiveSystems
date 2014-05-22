package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier
import main.scala.math.Vec3f
import main.scala.systems.gfx.{Texture, Shader, DrawFunction}
import org.lwjgl.opengl.GL11._
import main.scala.math.Mat4f
import org.lwjgl.opengl.GL20._
import java.nio.FloatBuffer
import org.lwjgl.BufferUtils
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 01.04.14 14:14
 */
object Collision extends ComponentCreator {

  val t = Texture.load("src/main/resources/meshes/VolumeBoxTexture.png")
  val texID = t.texId

  override def fromXML(xml: Node): Option[Collision] = xmlToComp[Collision](xml, "collision",n => {

    (n \ "AABB").foreach(aabb => {
      val lBB = (aabb \ "leftBottomBack").head
      val leftBottomBack = Vec3f((lBB \ "@x").text.toFloat,(lBB \ "@y").text.toFloat,(lBB \ "@z").text.toFloat)

      val rTF = (aabb \ "rightTopFront").head
      val rightTopFront = Vec3f((rTF \ "@x").text.toFloat,(rTF \ "@y").text.toFloat,(rTF \ "@z").text.toFloat)

      val col = new Collision(leftBottomBack, rightTopFront)

      return Some(col)

    })


    None
  })
}

class Collision(boundingVolume1: BoundingVolume = new AABB(Vec3f(),Vec3f())) extends Component {

  def this(leftBottomBack: Vec3f, rightTopFront: Vec3f) = this(new AABB(leftBottomBack,rightTopFront))

  private val _boundingVolume: BoundingVolume = boundingVolume1
  _boundingVolume.setOwner(this)


  def boundingVolume: BoundingVolume = _boundingVolume


  def updateBoundingVolume(matrix: Mat4f) = {
    boundingVolume.update(matrix)
    //println("updateBV",position.inline)
  }



  override def newInstance(identifier: Identifier): Component = boundingVolume match {
    case aabb: AABB => new Collision(aabb.leftBottomBack,aabb.rightTopFront)
    case _ => null
  }

  override def toXML: Node = {
    <collision>
      {boundingVolumeToXML()}
    </collision>
  }

  
  def boundingVolumeToXML(): Node = {
    boundingVolume match {
      case aabb: AABB =>
        <AABB>
          <leftBottomBack x={aabb.leftBottomBack.x.toString} y={aabb.leftBottomBack.y.toString} z={aabb.leftBottomBack.y.toString} />
          <rightTopFront x={aabb.rightTopFront.x.toString} y={aabb.rightTopFront.x.toString} z={aabb.rightTopFront.x.toString} />
        </AABB>
      case _ => <TODO></TODO>
    }
  }
}

object BoundingVolumeType extends Enumeration {
  val AABB, Sphere = Value //axis-aligned bounding box (AABB)
}



trait BoundingVolume extends DrawFunction {
  def centroid(): Vec3f

  def center(a: Float, b: Float): Float = (a+b)/2f

  def update(mat: Mat4f): Array[BBEndPoint]

  def owner: Collision
  def setOwner(own: Collision)



}
case class Sphere(cent: Vec3f, radius: Float) extends BoundingVolume{

  override def centroid(): Vec3f = cent

  override def update(mat: Mat4f): Array[BBEndPoint] = ???

  override def owner: Collision = ???

  override def setOwner(own: Collision): Unit = ???

  override def draw(shader: Shader, modelTransformation: Mat4f, viewMatrix: Mat4f, fov: Float, aspect: Option[Float], zNear: Float, zFar: Float, beforeFunc: (Unit) => Unit, afterFunc: (Unit) => Unit): Unit = ???
}

case class AABB(leftBottomBack: Vec3f, rightTopFront: Vec3f) extends BoundingVolume {

  private var _lbb: Vec3f = leftBottomBack
  private var _rtf: Vec3f = rightTopFront


  private var _owner: Collision = null
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

  override def owner: Collision = _owner

  override def setOwner(own: Collision): Unit = {
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

    Texture.bind(Collision.texID)

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

  def owner(): Collision = sub.owner

  override def toString: String = {
    if(isMin) ""+value+" min"
    else  ""+value+" max"
  }
}