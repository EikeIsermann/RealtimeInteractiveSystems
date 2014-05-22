package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:46
 */

case class Health(health1: Int = 0, maxHealth1: Int = 0) extends Component {
  private var _health: Int = health1
  private var _maxHealth: Int = maxHealth1

  def health: Int = _health
  def health_=(h: Int) = _health = h

  def maxHealth: Int = _maxHealth
  def maxHealth_=(mh: Int) = _maxHealth = mh

  override def toXML: Node = ???

  override def newInstance(i:Identifier): Component = new Health(health,maxHealth)
}
