package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 01.04.14 14:14
 */
object Collision extends ComponentCreator {

  override def fromXML(xml: Node): Option[Collision] = xmlToComp[Collision](xml, "collision",n => {

    (n \ "AABB").foreach(aabb => {
      val lBB = (aabb \ "leftBottomBack").head
      val leftBottomBack = Vec3f((lBB \ "@x").text.toFloat,(lBB \ "@y").text.toFloat,(lBB \ "@z").text.toFloat)

      val rTF = (aabb \ "rightTopFront").head
      val rightTopFront = Vec3f((rTF \ "@x").text.toFloat,(rTF \ "@y").text.toFloat,(rTF \ "@z").text.toFloat)

      return Some(new Collision(new AABB(leftBottomBack, rightTopFront)))

    })


    None
  })
}

class Collision(boundingVolume1: BoundingVolume = new AABB(Vec3f(0,0,0), Vec3f(0,0,0))) extends Component {


  private val _boundingVolume: BoundingVolume = boundingVolume1
  _boundingVolume setOwner this.identifier

  def boundingVolume: BoundingVolume = _boundingVolume

  def updateBoundingVolume(position: Vec3f) = {
    boundingVolume.update(position)
    println("updateBV",position.inline)
  }

  override def newInstance(identifier: Identifier): Component = new Collision(boundingVolume)

  override def toXML: Node = ???
}

object BoundingVolumeType extends Enumeration {
  val AABB, Sphere = Value //axis-aligned bounding box (AABB)
}



trait BoundingVolume {
  def setOwner(i: Identifier)

  def owner: Identifier

  def centroid(): Vec3f

  def center(a: Float, b: Float): Float = (a+b)/2f

  def update(position: Vec3f): Array[BBEndPoint]




}
case class Sphere(cent: Vec3f, radius: Float) extends BoundingVolume{

  override def centroid(): Vec3f = cent

  override def update(position: Vec3f): Array[BBEndPoint] = ???

  override def setOwner(i: Identifier): Unit = ???

  override def owner: Identifier = ???
}

case class AABB(leftBottomBack: Vec3f, rightTopFront: Vec3f) extends BoundingVolume {
  private var _owner: Identifier = null

  println("AABB",leftBottomBack,rightTopFront)
  val endPoints = Array(
   new BBEndPoint(leftBottomBack.x(),0,true)(this), //xMin
   new BBEndPoint(rightTopFront.x(),0,false)(this), //xMax
   new BBEndPoint(leftBottomBack.y(),1,true)(this), //yMin
   new BBEndPoint(rightTopFront.y(),1,false)(this), //yMax
   new BBEndPoint(leftBottomBack.z(),2,true)(this), //zMix
   new BBEndPoint(rightTopFront.z(),2,false)(this) //zMax
  )

  def interval(axis: Int): Array[BBEndPoint] = {
     axis match {
       case 0 => Array(endPoints(0),endPoints(1)) //x
       case 1 => Array(endPoints(2),endPoints(3)) //y
       case 2 => Array(endPoints(4),endPoints(5)) //z
     }
  }


  override def update(atPosition: Vec3f = Vec3f()): Array[BBEndPoint] = {
    endPoints.foreach(_.update(atPosition))
    println(endPoints.toList)
    endPoints
  }

  override def centroid(): Vec3f = Vec3f(center(endPoints(0).value,endPoints(1).value),center(endPoints(2).value,endPoints(3).value),center(endPoints(4).value,endPoints(5).value))



  override def setOwner(i: Identifier): Unit = {
    _owner = i
  }

  override def owner: Identifier = _owner
}


case class BBEndPoint(var value: Float, axis: Int, isMin: Boolean)(implicit sub: BoundingVolume) {
  println("BBEndPoint",value,axis,isMin)

  def update(v: Float) {value = value + v}
  def update(vec: Vec3f) {
    println("update a with ",axis,vec.inline)
    axis match {
      case 0 => update(vec.x)
      case 1 => update(vec.y)
      case 2 => update(vec.z)
    }
  }

  def owner(): Identifier = sub.owner

  override def toString: String = {
    if(isMin) ""+value+" min"
    else  ""+value+" max"
  }
}