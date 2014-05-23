package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.{BoundingVolume, AABB, Identifier}
import main.scala.math.Vec3f
import main.scala.systems.gfx.Texture
import main.scala.math.Mat4f

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

      val col = new Collision(leftBottomBack, rightTopFront)

      return Some(col)

    })


    None
  })
}

class Collision(boundingVolume1: BoundingVolume = new AABB(Vec3f(),Vec3f())) extends Component {

  def this(leftBottomBack: Vec3f, rightTopFront: Vec3f) = this(new AABB(leftBottomBack,rightTopFront))

  private var _boundingVolume: BoundingVolume = boundingVolume1
  _boundingVolume.setOwner(this)


  def boundingVolume: BoundingVolume = _boundingVolume
  def boundingVolume_=(bv:BoundingVolume) = _boundingVolume = bv


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

