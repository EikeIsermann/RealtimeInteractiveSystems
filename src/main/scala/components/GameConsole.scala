package main.scala.components

import main.scala.architecture.Component
import main.scala.tools.Identifier
import scala.xml.Node
import main.scala.systems.input.Key
import main.scala.entities.Entity
import main.scala.math.Vec3f
import main.scala.io.EntityTemplateLoader

/**
 * Created by Christian Treffs
 * Date: 21.05.14 22:26
 */
class GameConsole() extends Component {



  override def newInstance(identifier: Identifier): Component = new GameConsole()
  override def toXML: Node = ???
}
