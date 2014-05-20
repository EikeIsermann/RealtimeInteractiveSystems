package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:45
 */
object Camera extends ComponentCreator {
  def fromXML(xml: Node): Option[Camera] = {
    xmlToComp[Camera](xml, "camera", n => {
      val fov:Float = (n \ "fieldOfView").text.toFloat
      val asp:Float = (n \ "aspect").text.toFloat
      val nP:Float = (n \ "nearPlane").text.toFloat
      val fP:Float = (n \ "farPlane").text.toFloat
      val active:Boolean = (n \ "active").text.toBoolean

      Some(new Camera(fov, asp, nP,fP,active))
    })
  }

  final val defaultAspect: Float = 1f
  final val defaultFOV: Float = 90f
  final val defaultNearPlane: Float = 0.1f
  final val defaultFarPlane: Float = 50f
}

class Camera(fieldOfView1: Float = Camera.defaultFOV, aspect1:Float = Camera.defaultAspect, nearPlane1:Float = Camera.defaultNearPlane, farPlane1: Float = Camera.defaultFarPlane,active1: Boolean = true) extends Component {
  private var _aspect: Float = aspect1
  private var _fieldOfView: Float = fieldOfView1
  private var _nearPlane: Float = nearPlane1
  private var _farPlane: Float = farPlane1
  private var _active: Boolean = active1


  def aspect:Float = _aspect
  def aspect_=(a:Float) = _aspect = a

  def fieldOfView: Float = _fieldOfView
  def fieldOfView_=(fov:Float) = _fieldOfView = fov

  def nearPlane: Float = _nearPlane
  def nearPlane_=(nP: Float) = _nearPlane = nP

  def farPlane: Float = _farPlane
  def farPlane_=(fP: Float) = _farPlane = fP


  def active: Boolean = _active
  def active_=(state: Boolean) = _active = state

  override def toXML: Node = {
    <camera>
      <fieldOfView>{fieldOfView.toString}</fieldOfView>
      <aspect>{aspect.toString}</aspect>
      <nearPlane>{nearPlane.toString}</nearPlane>
      <farPlane>{farPlane.toString}</farPlane>
      <active>{active.toString}</active>
    </camera>
  }

  override def newInstance(i:Identifier): Component = new Camera(fieldOfView,aspect,nearPlane,farPlane,active)
}