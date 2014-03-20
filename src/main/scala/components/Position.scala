package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.NodeSeq
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:44
 */

object Position extends ComponentCreator {
  def fromXML(xml: NodeSeq): Component = {
    val pos = xml.head
    new Position(Vec3f(pos.attribute("x").get.text.toFloat,pos.attribute("y").get.text.toFloat, pos.attribute("z").get.text.toFloat))
  }
}
case class Position(x1: Float, y1: Float, z1: Float) extends Component {
  def this(pos: Vec3f) = this(pos.x, pos.y, pos.z)

  private var _vector: Vec3f = Vec3f(x1, y1, z1)

  def vec: Vec3f = _vector
  def vec_=(v: Vec3f) = _vector = v

  override def toXML: NodeSeq = <position x={vec.x} y={vec.y} z={vec.z} />
}
