package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.{Node, NodeBuffer, NodeSeq}
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:47
 */
object Projectile extends ComponentCreator {
  override def fromXML(xml: Node): Option[Projectile] = {
    xmlToComp[Projectile](xml, "projectile", n => {
      val damage: Float = (n \ "damage").text.toFloat
      Some(new Projectile(damage))
    })
  }
}

case class Projectile(damage1: Float) extends Component {

  private var _damage: Float = damage1
  def damage: Float = _damage
  def damage_=(d: Float) = _damage = d

  override def toXML: Node = {
    <projectile>
      <damage>{_damage.toString}</damage>
    </projectile>
  }

  override def newInstance(i:Identifier): Component = new Projectile(damage)

}
