package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.Node
import main.scala.systems.input.Triggers
import main.scala.math.Vec3f
import main.scala.tools.Identifier

/**
 * Created by Eike
 * 23.03.14.
 */
object CamControl extends ComponentCreator {
  override def fromXML(xml: Node): Option[CamControl] = {
    xmlToComp[CamControl](xml, "camControl", n => {

      val fW: Triggers = Triggers.fromXML((n \ "forward").head)
      val bW: Triggers = Triggers.fromXML((n \ "backward").head)
      val l: Triggers = Triggers.fromXML((n \ "left").head)
      val r: Triggers = Triggers.fromXML((n \ "right").head)
      val pP: Triggers = Triggers.fromXML((n \ "pitchPositive").head)
      val pN: Triggers = Triggers.fromXML((n \ "pitchNegative").head)
      val yL: Triggers = Triggers.fromXML((n \ "yawLeft").head)
      val yR: Triggers = Triggers.fromXML((n \ "yawRight").head)
      val sU: Triggers = Triggers.fromXML((n \ "stepUp").head)
      val sD: Triggers = Triggers.fromXML((n \ "stepDown").head)

      //TODO: velocities

      Some(new CamControl(fW,bW,l,r,pP,pN,yL,yR,sU,sD))
    })
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
                 triggerStepUp: Triggers,
                 triggerStepDown: Triggers,
                // movement, pitch, yaw
                 velocities: Vec3f = Vec3f(15f, 0.05f, 0.03f)) extends Component {

  private var _movementVel: Float = velocities.x
  private var _pitchVel: Float = velocities.y
  private var _yawVel: Float = velocities.z

  def movementVelocity: Float = _movementVel
  def movementVelocity_=(v: Float) = _movementVel = v

  def pitchVelocity: Float = _pitchVel
  def pitchVelocity_=(v: Float) = _pitchVel = v

  def yawVelocity: Float = _yawVel
  def yawVelocity_=(v: Float) = _yawVel = v

  override def toXML: Node = {
    <camControl>
      <forward>{triggerForward.toXML}</forward>
      <backward>{triggerBackward.toXML}</backward>
      <left>{triggerLeft.toXML}</left>
      <right>{triggerRight.toXML}</right>
      <pitchPositive>{triggerPitchPositive.toXML}</pitchPositive>
      <pitchNegative>{triggerPitchNegative.toXML}</pitchNegative>
      <yawLeft>{triggerYawLeft.toXML}</yawLeft>
      <yawRight>{triggerYawRight.toXML}</yawRight>
      <stepUp>{triggerStepUp.toXML}</stepUp>
      <stepDown>{triggerStepDown.toXML}</stepDown>
      <movementVel>{_movementVel.toString}</movementVel>
      <pitchVel>{_pitchVel.toString}</pitchVel>
      <yawVel>{_yawVel.toString}</yawVel>
    </camControl>
  }

  override def newInstance(i:Identifier): Component = new CamControl(triggerForward,triggerBackward,triggerLeft,triggerRight,triggerPitchPositive, triggerPitchNegative,triggerYawLeft,triggerYawRight,triggerStepUp,triggerStepDown,velocities)

}