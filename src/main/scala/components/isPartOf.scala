package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.entities.Entity

/**
 * Created by Christian Treffs
 * Date: 28.03.14 17:07
 */

object isPartOf extends ComponentCreator {
  override def fromXML(xml: Node): Component = {
    xmlToComp(xml, "ispartof", n => {

      //TODO: entity

      new isPartOf(null)
    })
  }
}

class isPartOf(entity1: Entity) extends Component {
  private var _entity: Entity = entity1

  def entity: Entity = _entity
  def entity_(e: Entity) = _entity = e

  //TODO: correct to actual name?!
  override def toXML: Node = {<ispartof>{entity.name}</ispartof>}
}
