package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import main.scala.systems.input.{MouseButton, MouseMovement, Key, Triggers}
import main.scala.tools.Identifier
import scala.xml.Node
import org.lwjgl.input.Mouse

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:06
 * This is a RIS Project class
 */
object GunControl extends ComponentCreator {
  override def fromXML(xml: Node): Option[GunControl] = {
    //TODO
    None
  }
}

case class GunControl(triggerYawLeft: Triggers = Triggers(Key.ArrowLeft, null,  MouseMovement.MovementX ),
                      triggerYawRight: Triggers  = Triggers(Key.ArrowRight, null,MouseMovement.MovementX),
                      triggerPitchPositive: Triggers = Triggers(Key.ArrowUp, null, MouseMovement.MovementY),
                      triggerPitchNegative: Triggers = Triggers(Key.ArrowDown, null, MouseMovement.MovementY),
                      triggerFire: Triggers = Triggers(Key.CtrlRight ,MouseButton.Left, null ),
                      triggerReload: Triggers = Triggers(), triggerSwitchAmmo: Triggers = Triggers()
                       ) extends Component {


  def newInstance(identifier: Identifier): Component = new GunControl(triggerYawLeft, triggerYawRight, triggerPitchPositive, triggerPitchNegative, triggerFire, triggerReload, triggerSwitchAmmo)

  def toXML: Node = {
    <gunControl>
      <yawLeft>{triggerYawLeft.toXML}</yawLeft>
      <yawRight>{triggerYawRight.toXML}</yawRight>
      <pitchPositive>{triggerPitchPositive.toXML}</pitchPositive>
      <pitchNegative>{triggerPitchNegative.toXML}</pitchNegative>
      <fire>{triggerFire.toXML}</fire>
      <reload>{triggerReload.toXML}</reload>
    </gunControl>
  }
}
