package main.scala.components

import main.scala.architecture.Component
import main.scala.systems.input.{Key, Triggers}
import main.scala.tools.Identifier
import scala.xml.Node

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:06
 * This is a RIS Project class
 */
case class GunControl(triggerYawLeft: Triggers = Triggers(Key.ArrowLeft), triggerYawRight: Triggers  = Triggers(Key.ArrowRight), triggerPitchPositive: Triggers = Triggers(Key.ArrowUp),
                  triggerPitchNegative: Triggers = Triggers(Key.ArrowDown), triggerFire: Triggers = Triggers(),
                  triggerReload: Triggers = Triggers(), triggerSwitchAmmo: Triggers = Triggers()) extends Component {

  def toXML: Node = {
    null
  }

  def newInstance(identifier: Identifier): Component = new GunControl(triggerYawLeft, triggerYawRight, triggerPitchPositive,
    triggerPitchNegative, triggerFire,
    triggerReload, triggerSwitchAmmo)
}
