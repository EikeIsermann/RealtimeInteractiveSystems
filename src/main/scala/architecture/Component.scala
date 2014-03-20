package main.scala.architecture

import main.scala.event.ObservingActor
import main.scala.systems.input.Context
import scala.xml.NodeSeq

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

  def toXML: NodeSeq
}
trait ComponentCreator {
  def fromXML(xml: NodeSeq): Component
}
