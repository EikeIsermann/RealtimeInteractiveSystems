package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import scala.collection.mutable.ArrayBuffer
import main.scala.entities.Entity
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 31.03.14 18:32
 */
class Children() extends Component{

  private var _children =  ArrayBuffer.empty[Entity]

  def children: Seq[Entity] = _children.toSeq

  def +=(e: Entity): Children = {
    _children += e
    this
  }

  override def toXML: Node =
    <hasParts>
      {children.map(c => <part>{c.identifier}</part>)}
    </hasParts>

  override def newInstance(i:Identifier) = new Children()
}
