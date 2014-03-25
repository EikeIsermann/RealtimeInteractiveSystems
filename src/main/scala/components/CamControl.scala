package main.scala.components

import main.scala.architecture.{Component, ComponentCreator}
import scala.xml.NodeSeq
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Eike
 * 23.03.14.
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
class CamControl(moveForward: Seq[Enumeration#Value],
                 moveBackwards: Seq[Enumeration#Value],
                 moveLeft: Seq[Enumeration#Value],
                 moveRight: Seq[Enumeration#Value],
                 pitchPos:  Seq[Enumeration#Value],
                 pitchNeg:  Seq[Enumeration#Value],
                 yawLeft:  Seq[Enumeration#Value],
                 yawRight:  Seq[Enumeration#Value],
                 movementVelocity: Float = 10f) extends Component {


  private var _goLeft = ArrayBuffer.empty[Enumeration#Value]
  private var _goRight = ArrayBuffer.empty[Enumeration#Value]
  private var _goForward = ArrayBuffer.empty[Enumeration#Value]
  private var _goBackward = ArrayBuffer.empty[Enumeration#Value]
  private var _pitchPositive = ArrayBuffer.empty[Enumeration#Value]
  private var _pitchNegative = ArrayBuffer.empty[Enumeration#Value]
  private var _yawLeft = ArrayBuffer.empty[Enumeration#Value]
  private var _yawRight = ArrayBuffer.empty[Enumeration#Value]
  private var _velocity: Float = movementVelocity


  _goLeft ++= moveLeft
  _goRight ++= moveRight
  _goForward ++= moveForward
  _goBackward ++= moveBackwards
  _pitchNegative ++= pitchNeg
  _pitchPositive ++= pitchPos
  _yawLeft ++= yawLeft
  _yawRight ++= yawRight
  velocity = movementVelocity


  def triggerLeft: Seq[Enumeration#Value] = _goLeft.toSeq
  def addTriggerLeft(trigger:Enumeration#Value) = _goLeft += trigger
  def removeTriggerLeft(trigger:Enumeration#Value) = _goLeft -= trigger

  def triggerRight: Seq[Enumeration#Value] = _goRight.toSeq
  def addTriggerRight(trigger:Enumeration#Value) = _goRight += trigger
  def removeTriggerRight(trigger:Enumeration#Value) = _goRight -= trigger

  def triggerForward: Seq[Enumeration#Value] = _goForward.toSeq
  def addTriggerForward(trigger:Enumeration#Value) = _goForward += trigger
  def removeTriggerForward(trigger:Enumeration#Value) = _goForward -= trigger

  def triggerBackward: Seq[Enumeration#Value] = _goBackward.toSeq
  def addTriggerBackward(trigger:Enumeration#Value) = _goBackward += trigger
  def removeTriggerBackward(trigger:Enumeration#Value) = _goBackward -= trigger

  def triggerPitchPositive: Seq[Enumeration#Value] = _pitchPositive.toSeq
  def addTriggerPitchPositive(trigger:Enumeration#Value) = _pitchPositive += trigger
  def removeTriggerPitchPositive(trigger:Enumeration#Value) = _pitchPositive -= trigger

  def triggerPitchNegative: Seq[Enumeration#Value] = _pitchNegative.toSeq
  def addTriggerPitchNegative(trigger:Enumeration#Value) = _pitchNegative += trigger
  def removeTriggerPitchNegative(trigger:Enumeration#Value) = _pitchNegative -= trigger

  def triggerYawLeft: Seq[Enumeration#Value] = _yawLeft.toSeq
  def addTriggerYawLeft(trigger:Enumeration#Value) = _yawLeft += trigger
  def removeTriggerYawLeft(trigger:Enumeration#Value) = _yawLeft -= trigger

  def triggerYawRight: Seq[Enumeration#Value] = _yawRight.toSeq
  def addTriggerYawRight(trigger:Enumeration#Value) = _yawRight += trigger
  def removeTriggerYawRight(trigger:Enumeration#Value) = _yawRight -= trigger

  def velocity: Float = _velocity
  def velocity_=(v: Float) = _velocity = v

  //override def toXML: NodeSeq = {<position x={vec.x} y={vec.y} z={vec.z} />}
  override def toXML: NodeSeq = ???
}