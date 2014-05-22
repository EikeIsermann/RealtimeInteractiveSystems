package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:46
 */
object Health extends ComponentCreator {
  override def fromXML(xml: Node): Option[Health] = xmlToComp[Health](xml,"health",n => {

    val health: Int = (n \ "@current").text.toInt
    val maxHealth: Int = (n \ "@max").text.toInt

    Some(new Health(health,maxHealth))
  })
}

case class Health(health1: Int = 0, maxHealth1: Int = 0) extends Component {
  private var _health: Int = health1
  private var _maxHealth: Int = maxHealth1

  def health: Int = _health
  def health_=(h: Int) = _health = h

  def maxHealth: Int = _maxHealth
  def maxHealth_=(mh: Int) = _maxHealth = mh

  def damage(d: Int) {
    _health -= d
    println("DAMAGE",health)
  }
  def heal(h: Int){_health += h}

  override def toXML: Node = {
    <health current={health.toString} max={maxHealth.toString}/>
  }

  override def newInstance(i:Identifier): Component = new Health(health,maxHealth)
}
