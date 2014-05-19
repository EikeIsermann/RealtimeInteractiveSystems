package main.scala.systems.input

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.GunControlNode
import main.scala.components.{Placement, GunControl, Gun}
import main.scala.math.Vec3f

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:29
 * This is a RIS Project class
 */
class GunControlSystem extends ProcessingSystem {
  var node: Class[_ <: Node] = classOf[GunControlNode]
  var priority: Int = 1

  /**
   * called on system startup
   * @return
   */
  def init(): System = {
    this
  }

  /**
   * executed before nodes are processed - every update
   */
  def begin(): Unit = {
    println(family._entities)
  }

  /**
   * executed after nodes are processed - every update
   */
  def end(): Unit = {}

  /**
   * called on system shutdown
   */
  def deinit(): Unit = {}

  def processNode(node: Node): Unit = {

    node match{
      case gunCon: GunControlNode =>
          val gun = node -> classOf[Gun]
          val control = node -> classOf[GunControl]
          val pos = node -> classOf[Placement]
          var pitch = pos.rotation.x
          var yaw = pos.rotation.y

        //PITCH POSITIVE/UP
        doAction(control.triggerPitchPositive, _ => {pitch += 1}, delta => motionDirectionViaMouse(delta))

        //PITCH NEGATIVE/DOWN
        doAction(control.triggerPitchNegative, _ => {pitch -= 1}, delta => motionDirectionViaMouse(delta))

        //YAW NEGATIVE/LEFT
        doAction(control.triggerYawLeft, _ => {yaw = (yaw + 1) % 360}, delta => motionDirectionViaMouse(delta))

        //YAW POSITIVE/RIGHT
        doAction(control.triggerYawRight, _ => {yaw = (yaw - 1) % 360}, delta => motionDirectionViaMouse(delta))


        def motionDirectionViaMouse(mouseDeltaNew: Vec3f) {
         /*
          // calculate current mouse delta - considering velocities/sensitivities
          val deltaX: Float = (mouseDeltaNew.x - mouseDelta.x) * pitchSensitivity
          val deltaY: Float = (mouseDeltaNew.y - mouseDelta.y) * yawSensitivity

          // update old mouse delta
          mouseDelta = mouseDeltaNew

          // sum deltas to get a pitch and yaw delta
          pitchDelta += deltaY
          yawDelta += deltaX

          //calculate pitch and yaw
          yaw -= yawDelta
          pitch +=  pitchDelta

          // constrain pitch
          if (pitch > 90.0f)  pitch = 90.0f
          if (pitch < -90.0f) pitch = -90.0f

          //constrain yaw
          if (yaw < -180.0f) yaw += 360.0f
          if (yaw > 180.0f)   yaw -= 360.0f

          //calculate radians
          xRad = math.sin(math.toRadians(-yaw)).toFloat
          zRad = math.cos(math.toRadians(-yaw)).toFloat
          yRad = math.sin(math.toRadians(pitch)).toFloat
            */
        }
          pos.rotation = Vec3f(pitch,yaw, pos.rotation.z)
          println(pos.rotation)

      case _ =>

    }




  }
  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit){
    Input.mouseMovementDo(triggers.mouseMovement, delta => mouseAction(delta))
    Input.keyDownDo(triggers.key, _ => keyAction())
  }



}
