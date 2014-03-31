package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.entities.Entity
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 31.03.14 18:28
 */



class Parent(parentEntity1: Entity) extends Component {
  private val _entity: Entity = parentEntity1

  def entity: Entity = _entity

  override def toXML: Node = ???

  override def newInstance(i:Identifier): Component = new Parent(entity)
}
