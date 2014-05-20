package main.scala.components

import main.scala.architecture.Component
import main.scala.systems.input.{MouseMovement, Key, Triggers}
import main.scala.tools.Identifier
import scala.xml.Node

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:06
 * This is a RIS Project class
 */
case class GunControl(triggerYawLeft: Triggers = Triggers(Key.ArrowLeft, null, MouseMovement.MovementX ), triggerYawRight: Triggers  = Triggers(Key.ArrowRight, null, MouseMovement.MovementX)
                      , triggerPitchPositive: Triggers = Triggers(Key.ArrowUp, null, MouseMovement.MovementY),
                  triggerPitchNegative: Triggers = Triggers(Key.ArrowDown, null, MouseMovement.MovementY), triggerFire: Triggers = Triggers(),
                  triggerReload: Triggers = Triggers(), triggerSwitchAmmo: Triggers = Triggers()) extends Component {

  def toXML: Node = {
    null
  }

  def newInstance(identifier: Identifier): Component = new GunControl(triggerYawLeft, triggerYawRight, triggerPitchPositive,
    triggerPitchNegative, triggerFire,
    triggerReload, triggerSwitchAmmo)
}
