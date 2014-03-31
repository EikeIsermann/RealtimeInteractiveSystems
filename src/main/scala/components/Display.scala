package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
object Display extends ComponentCreator {
  override def fromXML(xml: Node): Option[Display] = xmlToComp[Display](xml, "display",n => {

     val meshId:Symbol = Symbol((n\"meshId").text)
     val shaderId:Symbol = Symbol((n\"shaderId").text)

    Some(new Display(meshId, shaderId))
  })
}

class Display(meshId1: Symbol = '_undefined, shaderId1: Symbol = '_undefined) extends Component {

  private var _meshId: Symbol = meshId1
  private var _shaderId: Symbol = shaderId1

  def meshId: Symbol = _meshId
  def meshId_=(m: Symbol) = _meshId = m

  def shaderId: Symbol = _shaderId
  def shaderId_=(s: Symbol) = _shaderId = s

  override def toXML: Node = {<display>
    <meshId>{meshId.name.toString}</meshId>
    <shaderId>{shaderId.name.toString}</shaderId>
  </display>}

  override def newInstance(i:Identifier): Component = new Display(meshId,shaderId)
}
