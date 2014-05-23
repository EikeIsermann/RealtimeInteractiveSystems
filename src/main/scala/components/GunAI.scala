package main.scala.components

import main.scala.systems.ai.aiStates.{gunSearching, AIState}
import main.scala.architecture.{ComponentCreator, Component}
import main.scala.tools.Identifier
import scala.xml.Node

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:19
 * This is a RIS Project class
 */
object GunAI extends ComponentCreator {
  override def fromXML(xml: Node): Option[GunAI] = xmlToComp[GunAI](xml, "gunAI", n => Some(new GunAI()))

}

case class GunAI() extends Component {

  var state: AIState = new gunSearching()

  def newInstance(identifier: Identifier): Component = new GunAI()

  def toXML: Node = {
    <gunAI></gunAI>
  }

}
