package main.scala.components

import main.scala.systems.gfx.{Shader, Mesh}
import main.scala.architecture.Component
import scala.xml.NodeSeq

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
case class Display(mesh1: Mesh, shader1: Shader) extends Component {
  private var _mesh: Mesh = mesh1
  private var _shader: Shader = shader1

  def mesh: Mesh = _mesh
  def mesh_=(m: Mesh) = _mesh = m

  def shader: Shader = _shader
  def shader_=(s: Shader) = _shader = s

  override def toXML: NodeSeq = ???

}
