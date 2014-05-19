package main.scala.components

import main.scala.architecture.Component
import main.scala.systems.input.Triggers

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:06
 * This is a RIS Project class
 */
case class DriveControl(triggerForward: Triggers, triggerBackward: Triggers, triggerLeft: Triggers,
                        triggerRight: Triggers, triggerBoost: Triggers) extends Component {

}
