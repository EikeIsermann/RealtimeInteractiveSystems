package main.scala.architecture

import scala.xml.NodeBuffer
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 14.03.14 17:43
 *
 * Components are simple value object.
 * The raw data for one aspect of the object, and how it interacts with the world.
 * Labels the Entity as possessing this particular aspect.
 *
 */
trait Component /*extends ObservingActor*/ {
  private val _identifier: Identifier = Identifier.create(this.getClass.getSimpleName)

  def identifier: Identifier = _identifier

  def toXML: NodeBuffer

  override def toString: String = "[Component] "+identifier.toString
}
trait ComponentCreator {
  def fromXML(xml: scala.xml.Node): Component
}
