package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node

/**
 * Created by Christian Treffs
 * Date: 28.03.14 17:07
 */

object isPartOf extends ComponentCreator {
  override def fromXML(xml: Node): Option[isPartOf] = xmlToComp(xml, "isPartOf", n => {
     val partOf = Symbol(n.text)
    
      Some(new isPartOf(partOf))
    })
  }


class isPartOf(part1: Symbol) extends Component {
  private val _part: Symbol = part1
  def part: Symbol = _part
  //TODO: correct to actual name?!
  override def toXML: Node = {<isPartOf>{part.name}</isPartOf>}
}
