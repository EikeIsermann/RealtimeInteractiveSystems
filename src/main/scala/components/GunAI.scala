package main.scala.components

import main.scala.systems.ai.aiStates.{gunSearching, AIState}
import main.scala.architecture.Component
import main.scala.tools.Identifier
import scala.xml.Node

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:19
 * This is a RIS Project class
 */
case class GunAI() extends Component {

  var state: AIState = new gunSearching()

  def toXML: Node = {
    null
  }

  def newInstance(identifier: Identifier): Component = {
     null
      }

}
