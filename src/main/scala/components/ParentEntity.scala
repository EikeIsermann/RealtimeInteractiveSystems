package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.{NodeBuffer, NodeSeq}
import main.scala.tools.Identifier


/**
 * Created by Christian Treffs
 * Date: 21.03.14 10:05
 */

object ParentEntity extends ComponentCreator{
  override def fromXML(xml: NodeBuffer): Component = ???
}
case class ParentEntity(parentIdentifier: Identifier) extends Component{
  override def toXML: NodeBuffer = ???
}