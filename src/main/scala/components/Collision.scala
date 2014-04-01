package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 01.04.14 14:14
 */
object Collision extends ComponentCreator {

  override def fromXML(xml: Node): Option[Collision] = ???
}

class Collision extends Component {
  override def newInstance(identifier: Identifier): Component = ???

  override def toXML: Node = ???
}
