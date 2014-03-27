package main.scala.components

import main.scala.architecture.Component
import scala.xml.NodeBuffer
import main.scala.entities.Entity

/**
 * Created by Christian Treffs
 * Date: 21.03.14 10:06
 */

case class ChildEntities(entities: Seq[Entity])  extends Component {

  override def toXML: NodeBuffer = ???
}
