package main.scala.systems.input

import main.scala.architecture.{System, ProcessingSystem, Node}
import main.scala.nodes.CamControlNode
import main.scala.components.{Placement, CamControl, Motion}
import main.scala.math.Vec3f
import main.scala.tools.DC

/**
 * Camera Control System
 * Created by Eike
 * 23.03.14.
 */
class CamControlSystem extends ProcessingSystem {

  var node: Class[_ <: Node] = classOf[CamControlNode]
  var priority = 0

  var x: Float = 0.0f
  var y: Float = 0.0f
  var z: Float = 0.0f

  var mouseDelta: Vec3f = Vec3f()

  var pitch: Float = 0.0f
  var pitchDelta: Float = 0.0f

  var yaw: Float = 0.0f
  var yawDelta: Float = 0.0f

  var xRad = math.sin(math.toRadians(-yaw)).toFloat
  var zRad = math.cos(math.toRadians(-yaw)).toFloat
  var  yRad = math.sin(math.toRadians(pitch)).toFloat


  def begin(): Unit = {

  }

  def end(): Unit = {

  }

  //TODO!!!
  override def processNode(node: Node): Unit = {
    node match {
        case camCon: CamControlNode =>
          val motion = node -> classOf[Motion]
          val control = node -> classOf[CamControl]


          val movementVelocity = control.movementVelocity * ctx.deltaT
          val pitchSensitivity = control.pitchVelocity
          val yawSensitivity = control.yawVelocity


          //PITCH POSITIVE/UP
          doAction(control.triggerPitchPositive, _ => {pitch += 1}, delta => motionDirectionViaMouse(delta))

          //PITCH NEGATIVE/DOWN
          doAction(control.triggerPitchNegative, _ => {pitch -= 1}, delta => motionDirectionViaMouse(delta))

          //YAW NEGATIVE/LEFT
          // doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit)
          doAction(control.triggerYawLeft, _ => {yaw = (yaw + 1) % 360}, delta => motionDirectionViaMouse(delta))

          //YAW POSITIVE/RIGHT
          doAction(control.triggerYawRight, _ => {yaw = (yaw - 1) % 360}, delta => motionDirectionViaMouse(delta))

          doAction(control.triggerStepUp, _ => {y+=(movementVelocity * 0.5f)}, delta => motionDirectionViaMouse(delta))

          doAction(control.triggerStepDown, _ => {y-=(movementVelocity * 0.5f)}, delta => motionDirectionViaMouse(delta))


          // doAction ( TRIGGER , KEYBOARD ACTION, MOUSE ACTION, CONTROLLER ACTION .... )
          //FORWARD
          doAction(control.triggerForward, _ => {

            x += movementVelocity * xRad * (1-Math.abs(yRad))
            z -= movementVelocity * zRad * (1-Math.abs(yRad))
            y += movementVelocity * yRad
          }, delta => {})

          //BACKWARD
          doAction(control.triggerBackward, _ => {
            x -= movementVelocity  * xRad * (1-Math.abs(yRad))
            z += movementVelocity * zRad * (1-Math.abs(yRad))
            y -= movementVelocity * yRad
          }, delta => {})

          //LEFT
          doAction(control.triggerLeft, _ => {
            x -= movementVelocity * math.sin(math.toRadians(-yaw + 90f)).toFloat
            z += movementVelocity * math.cos(math.toRadians(-yaw + 90f)).toFloat
          }, delta => {})

          //RIGHT
          doAction(control.triggerRight, _ => {
            x -= movementVelocity * math.sin(math.toRadians(-yaw - 90f)).toFloat
            z += movementVelocity * math.cos(math.toRadians(-yaw - 90f)).toFloat
          }, delta => {})


          def motionDirectionViaMouse(mouseDeltaNew: Vec3f) {

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

          }


          motion.velocity = Vec3f(x, y, z)
          //TODO: different
          motion.testRot = Vec3f(pitch, yaw, 0)
        case _ =>

    }
  }


  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit) {

    Input.mouseMovementDo(triggers.mouseMovement, delta => mouseAction(delta))
    Input.keyDownDo(triggers.key, _ => keyAction())

  }

  override def init(): System = {
    this
  }

  override def deinit(): Unit = {
  }

}
