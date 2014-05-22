package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:48
 */
object Light extends ComponentCreator {
  override def fromXML(xml: Node): Option[Light] = ???
}

case class Light() extends Component{
  override def toXML: Node = ???

  override def newInstance(i:Identifier): Component = new Light()
}
