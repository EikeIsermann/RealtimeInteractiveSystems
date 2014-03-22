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
    /*val pos = xml.head
    new Position(Vec3f(pos.attribute("x").get.text.toFloat,pos.attribute("y").get.text.toFloat, pos.attribute("z").get.text.toFloat))
    */
    //TODO
    null
  }
}
case class Position(pos: Vec3f, rot: Vec3f) extends Component {


  private var _position: Vec3f = pos
  private var _rotation: Vec3f = rot

  def position: Vec3f = _position
  def position_=(v: Vec3f) = _position = v

  def rotation: Vec3f = _rotation
  def rotation_=(v: Vec3f) = _rotation = v

  //override def toXML: NodeSeq = {<position x={vec.x} y={vec.y} z={vec.z} />}
  override def toXML: NodeSeq = ???
}
