package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.{Node, NodeSeq, NodeBuffer}
import main.scala.math.{Quat, Vec3f}
import main.scala.architecture
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
class Placement(position1: Vec3f = Vec3f(0,0,0), rotation1: Vec3f = Vec3f(0,0,0), scale1: Vec3f = Vec3f(1,1,1), off: Vec3f = Vec3f(0,0,0)) extends Component {



  private var _relativePosition: Vec3f = position1
  private var _relativeOrientation: Quat = Quat()
  private var _relativeScale: Vec3f = scale1
  private var _relativeRotation: Vec3f = rotation1

  private var _position: Vec3f = position1
  private var _orientation: Quat = Quat()
  private var _scale: Vec3f = scale1


  /**
   * linear position
   */
  def position: Vec3f = _position
  def position_=(v: Vec3f) = {_position = v ; _relativePosition = v}

  def orientation: Quat  = _orientation
  def orientation_=(o: Quat) = {_orientation = o ; _relativeOrientation = o}

  //TODO: remove
  private var _rotation: Vec3f = rotation1
  def rotation: Vec3f = _rotation
  def rotation_=(v: Vec3f) = {_rotation = v ; _relativeRotation = v  }

  def scale: Vec3f = _scale
  def scale_=(v: Vec3f) = {_scale = v ; _relativeScale = v}



  def relativeUpdate(p: Placement){

       _orientation = _relativeOrientation + p.orientation
       _rotation = _relativeRotation +  p.rotation
       _position = _relativePosition + p.position
       _scale = _relativeScale + p.scale
  }
  def setTo(p: Placement) = {
    position = p.position
    orientation = p.orientation
    rotation = p.rotation
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