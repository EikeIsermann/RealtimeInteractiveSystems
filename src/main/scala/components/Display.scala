package main.scala.components

import main.scala.systems.gfx.{Shader, Mesh}
import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.{Node, NodeBuffer, NodeSeq}
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
object Display extends ComponentCreator {
  override def fromXML(xml: Node): Component = {
    xmlToComp(xml, "display", n => {
      //TODO: read node info to component

      new Display()
    })
  }
}

class Display(meshId1: Symbol = '_undefined, shaderId1: Symbol = '_undefined) extends Component {

  private var _meshId: Symbol = meshId1
  private var _shaderId: Symbol = shaderId1

  def meshId: Symbol = _meshId
  def meshId_=(m: Symbol) = _meshId = m

  def shaderId: Symbol = _shaderId
  def shaderId_=(s: Symbol) = _shaderId = s

  override def toXML: Node = {<display></display>}
}
