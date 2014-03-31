package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.{Node, NodeBuffer, NodeSeq}
import main.scala.tools.Identifier
import main.scala.entities.Entity


/**
 * Created by Christian Treffs
 * Date: 21.03.14 10:05
 */

case class ParentEntity(entity: Entity) extends Component{
  override def toXML: Node = ???
}