package main.scala.components

import main.scala.architecture.Component
import main.scala.systems.ai.aiStates.AIState
import main.scala.tools.Identifier
import scala.xml.Node

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:25
 * This is a RIS Project class
 */
case class DriveAI(state: AIState) extends Component {
  def toXML: Node = {
      null
  }

  def newInstance(identifier: Identifier): Component = {
     null
  }
}
