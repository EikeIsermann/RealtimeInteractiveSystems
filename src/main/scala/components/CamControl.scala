package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.NodeSeq
import scala.collection.mutable.ArrayBuffer
import main.scala.systems.input.Triggers

/**
 * Created by Eike
 * 23.03.14.
 */
object CamControl extends ComponentCreator {
  def fromXML(xml: NodeSeq)  = {
    /*val pos = xml.head
    new Position(Vec3f(pos.attribute("x").get.text.toFloat,pos.attribute("y").get.text.toFloat, pos.attribute("z").get.text.toFloat))
    */
    //TODO
    null
  }
}
case class CamControl(triggerForward: Triggers,
                      triggerBackward: Triggers,
                 triggerLeft: Triggers,
                 triggerRight: Triggers,
                 triggerPitchPositive:  Triggers,
                 triggerPitchNegative:  Triggers,
                 triggerYawLeft:  Triggers,
                 triggerYawRight:  Triggers,
                 movementVelocity: Float = 10f) extends Component {



  private var _velocity: Float = movementVelocity

   velocity = movementVelocity


  def velocity: Float = _velocity
  def velocity_=(v: Float) = _velocity = v

  //override def toXML: NodeSeq = {<position x={vec.x} y={vec.y} z={vec.z} />}
  override def toXML: NodeSeq = ???
}