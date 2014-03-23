package main.scala.components

/**
 * Created by Eike on 23.03.14.
 */

import main.scala.math.Vec3f
import main.scala.architecture.Component
import scala.xml.NodeSeq
import main.scala.architecture.ComponentCreator


object Motion extends ComponentCreator {
  def fromXML(xml: NodeSeq): Component = {
    /*val pos = xml.head
    new Position(Vec3f(pos.attribute("x").get.text.toFloat,pos.attribute("y").get.text.toFloat, pos.attribute("z").get.text.toFloat))
    */
    //TODO
    null
  }
}
case class Motion(pos: Vec3f, rot: Vec3f) extends Component {


  private var _velocity: Vec3f = pos
  private var _friction: Float = 1f

  def velocity: Vec3f = _velocity
  def velocity_=(v: Vec3f) = _velocity = v

  def friction: Float = _friction
  def friction_=(f:Float) = _friction = f


  override def toXML: NodeSeq = ???
}
