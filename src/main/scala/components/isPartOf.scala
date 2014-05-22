package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 28.03.14 17:07
 */

object isPartOf extends ComponentCreator {
  override def fromXML(xml: Node): Option[isPartOf] = xmlToComp(xml, "isPartOf", n => {
    if(n.text.trim.isEmpty) {
      return None
    }

      val partOf = Symbol(n.text.trim)
      Some(new isPartOf(partOf))
    })
  }


class isPartOf(part1: Symbol) extends Component {

  private val _part: Symbol = part1
  def part: Symbol = _part
  //TODO: correct to actual name?!
  //<isPartOf>{part.name}</isPartOf>
  override def toXML: Node = null

  override def newInstance(i:Identifier): Component = new isPartOf(part)

}
