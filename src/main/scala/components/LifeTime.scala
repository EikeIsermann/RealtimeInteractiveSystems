package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:47
 */
case class LifeTime() extends Component{
  override def toXML: Node = ???

  override def newInstance(i:Identifier): Component = new LifeTime

}
