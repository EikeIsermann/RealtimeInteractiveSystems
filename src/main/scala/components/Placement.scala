package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.Node
import main.scala.math.{Mat4f, Quat, Vec3f}
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:44
 */

object Placement extends ComponentCreator {

  def fromXML(xml: Node): Option[Placement] = {
    xmlToComp[Placement](xml, "placement", n => {
      val pos = n \ "position"
      val rot = n \ "rotation"
      val sc = n \ "scale"

      val posVec = Vec3f((pos \ "@x").text.toFloat,(pos \ "@y").text.toFloat,(pos \ "@z").text.toFloat)
      val rotVec = Vec3f((rot \ "@angleX").text.toFloat,(rot \ "@angleY").text.toFloat,(rot \ "@angleZ").text.toFloat)
      val scVec = Vec3f((sc \ "@x").text.toFloat,(sc \ "@y").text.toFloat,(sc \ "@z").text.toFloat)

      Some(new Placement(posVec, rotVec, scVec))
    })
  }
}
class Placement(position1: Vec3f = Vec3f(0,0,0), rotation1: Vec3f = Vec3f(0,0,0), scale1: Vec3f = Vec3f(1,1,1)) extends Component {



  private var _relativePosition: Vec3f = position1
  private var _relativeOrientation: Quat = Quat()
  private var _relativeScale: Vec3f = scale1
  private var _relativeRotation: Vec3f = rotation1

  private var _basePosition: Mat4f = Mat4f.identity

  /**
   * linear position
   */
  def position: Vec3f = _relativePosition
  def position_=(v: Vec3f) = {
    _relativePosition = v
  }

  def orientation: Quat  = _relativeOrientation
  def orientation_=(o: Quat) = { _relativeOrientation = o}


  //TODO: remove
  def rotation: Vec3f = _relativeRotation
  def rotation_=(v: Vec3f) = {_relativeRotation = v  }

  def scale: Vec3f = _relativeScale
  def scale_=(v: Vec3f) = {_relativeScale = v}

  def basePosition = _basePosition

  def getMatrix: Mat4f = {
    if(!(_basePosition == Mat4f.identity))  Mat4f.translation(position)  * Mat4f.rotation(rotation) * Mat4f.scale(scale) * _basePosition
    else Mat4f.rotation(rotation) * Mat4f.translation(position)  *  Mat4f.scale(scale)

  }


  def relativeUpdate(p: Mat4f){
      _basePosition = p
  }

  def setTo(p: Placement) = {
    position = p.position
    orientation = p.orientation
    rotation = p.rotation
    _basePosition = p.basePosition
  }



  override def toXML: Node = {
    <placement>
      <position x={ position.x.toString } y={ position.y.toString } z={ position.z.toString } />
      <rotation angleX={ rotation.x.toString } angleY={ rotation.y.toString } angleZ={ rotation.z.toString } />
      <scale x={scale.x.toString} y={scale.y.toString} z={scale.z.toString} />
    </placement>
  }

  override def newInstance(i:Identifier): Component = new Placement(position,rotation,scale)
}