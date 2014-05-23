package main.scala.components

import main.scala.architecture.Component
import main.scala.tools.Identifier
import scala.xml.Node

/**
 * Created by Christian Treffs
 * Date: 23.05.14 07:04
 */
class MakeItSolid extends Component {
  override def toXML: Node = {<makeIdSolid/>}

  override def newInstance(identifier: Identifier): Component = new MakeItSolid()
}
