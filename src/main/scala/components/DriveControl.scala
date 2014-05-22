package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import main.scala.systems.input.{Key, Triggers}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:06
 * This is a RIS Project class
 */
object DriveControl extends ComponentCreator {
  override def fromXML(xml: Node): Option[DriveControl] = {
    xmlToComp[DriveControl](xml, "driveControl", n => {

      val fwd: Triggers = Triggers.fromXML((n \ "forward").head)
      val bwd: Triggers = Triggers.fromXML((n \ "backward").head)
      val lft: Triggers = Triggers.fromXML((n \ "left").head)
      val rgt: Triggers = Triggers.fromXML((n \ "right").head)
      val boo: Triggers = Triggers.fromXML((n \ "boost").head)

      Some(new DriveControl(fwd,bwd,lft,rgt,boo))
    })
  }
}

case class DriveControl(triggerForward: Triggers = Triggers(Key._W),
                        triggerBackward: Triggers = Triggers(Key._S),
                        triggerLeft: Triggers = Triggers(Key._A),
                        triggerRight: Triggers = Triggers(Key._D),
                        triggerBoost: Triggers = Triggers(Key.ShiftLeft)
                         ) extends Component {

  def newInstance(identifier: Identifier): Component = new DriveControl(triggerForward, triggerBackward, triggerLeft, triggerRight, triggerBoost)

  def toXML: Node = {
    <driveControl>
      <forward>{triggerForward.toXML}</forward>
      <backward>{triggerBackward.toXML}</backward>
      <left>{triggerLeft.toXML}</left>
      <right>{triggerRight.toXML}</right>
      <boost>{triggerBoost.toXML}</boost>
    </driveControl>
  }
}
