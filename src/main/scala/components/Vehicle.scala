package main.scala.components

import main.scala.architecture.Component
import main.scala.tools.Identifier
import scala.xml.Node


/**
 * User: uni
 * Date: 20.05.14
 * Time: 12:46
 * This is a RIS Project class
 */
case class Vehicle(power: Float = 100000, boostFactor:Float = 3f , turnSpeed: Float = 1f) extends Component {
  def toXML: Node = {
    null
  }

  def newInstance(identifier: Identifier): Component = new Vehicle(power)
}
