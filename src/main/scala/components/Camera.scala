package main.scala.components

import main.scala.architecture.Component
import scala.xml.{Node, NodeBuffer, NodeSeq}
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
case class Camera(fieldOfView1: Float, active1: Boolean = true) extends Component {
  private var _fov: Float = fieldOfView1
  private var _active: Boolean = active1

  def fieldOfView: Float = _fov
  def fieldOfView_=(fov: Float) = _fov = fov

  def active: Boolean = _active
  def active_=(state: Boolean) = _active = state

  override def toXML: Node = ???

  override def newInstance(i:Identifier): Component = new Camera(fieldOfView,active)
}