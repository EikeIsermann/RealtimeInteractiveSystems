package main.scala.components

import main.scala.systems.input.Key
import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.NodeSeq

/**
 * Created by Eike on 23.03.14.
 */
object CamControl extends ComponentCreator {
  def fromXML(xml: NodeSeq)  = {
    /*val pos = xml.head
    new Position(Vec3f(pos.attribute("x").get.text.toFloat,pos.attribute("y").get.text.toFloat, pos.attribute("z").get.text.toFloat))
    */
    //TODO
    null
  }
}
class CamControl(lK: Key.Value, uK: Key.Value, rK: Key.Value, dK: Key.Value) extends Component {


  private var _leftKey: Key.Value = lK
  private var _rightKey: Key.Value = rK
  private var _upKey: Key.Value = uK
  private var _downKey: Key.Value = dK
  private var _velocity: Float = 0.4f

  def velocity: Float = _velocity
  def velocity_=(v: Float) = _velocity = v

  def leftKey: Key.Value = _leftKey
  def leftKey_=(v: Key.Value) = _leftKey = v


  def upKey: Key.Value = _upKey
  def upKey_=(v: Key.Value) = _upKey = v


  def rightKey: Key.Value = _rightKey
  def rightKey_=(v: Key.Value) = _rightKey =  v


  def downKey: Key.Value = _downKey
  def downKey_=(v: Key.Value) = _downKey = v


  //override def toXML: NodeSeq = {<position x={vec.x} y={vec.y} z={vec.z} />}
  override def toXML: NodeSeq = ???
}

