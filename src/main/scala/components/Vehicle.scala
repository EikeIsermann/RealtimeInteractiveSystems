package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import main.scala.tools.Identifier
import scala.xml.Node


/**
 * User: uni
 * Date: 20.05.14
 * Time: 12:46
 * This is a RIS Project class
 */
object Vehicle extends ComponentCreator {
  override def fromXML(xml: Node): Option[Vehicle] = {
    xmlToComp[Vehicle](xml, "vehicle", n => {

      val pw: Float = (n \ "power").text.toFloat
      val bo: Float = (n \ "boostFactor").text.toFloat
      val ts: Float = (n \ "turnSpeed").text.toFloat

      Some(new Vehicle(pw,bo,ts))
    })
  }
}

case class Vehicle(power: Float = 100000, boostFactor:Float = 3f , turnSpeed: Float = 1f) extends Component {
  def newInstance(identifier: Identifier): Component = new Vehicle(power)

  def toXML: Node = {
    <vehicle>
      <power>{power.toString}</power>
      <boostFactor>{boostFactor.toString}</boostFactor>
      <turnSpeed>{turnSpeed.toString}</turnSpeed>
    </vehicle>
  }
}
