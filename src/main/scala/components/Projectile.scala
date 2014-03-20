package main.scala.components

import main.scala.architecture.Component
import scala.xml.NodeSeq

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:47
 */
case class Projectile(damage1: Float) extends Component {
  private var _damage: Float = damage1
  def damage: Float = _damage
  def damage_=(d: Float) = _damage = d

  override def toXML: NodeSeq = ???


}
