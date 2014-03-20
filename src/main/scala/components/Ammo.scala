package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.NodeSeq

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
object Ammo extends ComponentCreator {
  def fromXML(xml: NodeSeq): Component = {
    val a = xml.head
    new Ammo(a.attribute("start").get.text.toInt, a.attribute("max").get.text.toInt)
  }
}
case class Ammo(ammo1: Int, maxAmmo1: Int) extends Component {
  private var _ammo: Int = ammo1
  private var _maxAmmo: Int = maxAmmo1

  def ammo: Int = _ammo
  def ammo_=(a: Int) = _ammo = a

  def maxAmmo: Int = _maxAmmo
  def maxAmmo_=(a: Int) = _maxAmmo = a

  override def toXML: NodeSeq = <ammo start={ammo} max={maxAmmo} />
}
