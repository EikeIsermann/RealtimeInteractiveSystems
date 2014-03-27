package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.NodeSeq
import main.scala.systems.input.Triggers
import main.scala.math.Vec3f

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
                // movement, pitch, yaw
                 velocities: Vec3f = Vec3f(1.5f, 0.05f, 0.03f)) extends Component {

  private var _movementVel: Float = velocities.x
  private var _pitchVel: Float = velocities.y
  private var _yawVel: Float = velocities.z

  def movementVelocity: Float = _movementVel
  def movementVelocity_=(v: Float) = _movementVel = v

  def pitchVelocity: Float = _pitchVel
  def pitchVelocity_=(v: Float) = _pitchVel = v

  def yawVelocity: Float = _yawVel
  def yawVelocity_=(v: Float) = _yawVel = v

  //override def toXML: NodeSeq = {<position x={vec.x} y={vec.y} z={vec.z} />}
  override def toXML: NodeSeq = ???
}