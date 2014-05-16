package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.entities.EntityManager
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 31.03.14 18:28
 */



class Parent(parentEntity1: EntityManager) extends Component {
  private val _entity: EntityManager = parentEntity1

  def entity: EntityManager = _entity

  override def toXML: Node = {
    <parent>{entity.identifier}</parent>
  }

  override def newInstance(i:Identifier): Component = new Parent(entity)
}
