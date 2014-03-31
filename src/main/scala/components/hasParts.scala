package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import scala.collection.mutable.ListBuffer

/**
 * Created by Christian Treffs
 * Date: 28.03.14 17:07
 */

object hasParts extends ComponentCreator {

  override def fromXML(xml: Node): Option[hasParts] = xmlToComp[hasParts](xml,"hasParts",n => {
    val parts = ListBuffer.empty[Symbol]
    n \\ "part" foreach (p => {
      parts += Symbol(p.text.toString)
    })
    Some(new hasParts(parts.toSeq))
  })
}

class hasParts(parts1: Seq[Symbol]) extends Component {

  private val _parts = parts1

  def parts: Seq[Symbol] = _parts

  override def toXML: Node = {
    <hasParts>
      { parts.foreach(p => <part>{p.name}</part>) }
    </hasParts>
  }
}
