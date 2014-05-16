package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.{Node, NodeBuffer, NodeSeq}
import main.scala.tools.Identifier
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
object Camera extends ComponentCreator {
  def fromXML(xml: Node): Option[Camera] = {
    xmlToComp[Camera](xml, "camera", n => {
      val fov = n \ "fieldOfView"
      val active = n \ "active"

      Some(new Camera(fov.toString().toFloat, active.toString().toBoolean))
    })
  }
}

case class Camera(fieldOfView1: Float, active1: Boolean = true) extends Component {
  private var _fov: Float = fieldOfView1
  private var _active: Boolean = active1

  def fieldOfView: Float = _fov
  def fieldOfView_=(fov: Float) = _fov = fov

  def active: Boolean = _active
  def active_=(state: Boolean) = _active = state

  override def toXML: Node = {
    <camera>
      <fieldOfView>{fieldOfView}</fieldOfView>
      <active>{active}</active>
    </camera>
  }

  override def newInstance(i:Identifier): Component = new Camera(fieldOfView,active)
}