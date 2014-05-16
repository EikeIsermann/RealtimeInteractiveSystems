package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import scala.collection.mutable.ArrayBuffer
import main.scala.entities.EntityManager
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 31.03.14 18:32
 */
class Children() extends Component{

  private var _children =  ArrayBuffer.empty[EntityManager]

  def children: Seq[EntityManager] = _children.toSeq

  def +=(e: EntityManager): Children = {
    _children += e
    this
  }

  override def toXML: Node =
    <children>
      {children.map(c => <entity>{c.identifier.toString}</entity>)}
    </children>


  override def newInstance(i:Identifier) = new Children()
}
