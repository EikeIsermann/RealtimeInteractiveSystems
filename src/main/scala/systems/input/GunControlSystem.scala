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
  var ds: Float = 1
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
    ds = (ctx.deltaT) * 100
    //println(family._entities)
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
        doAction(control.triggerPitchPositive, _ => {pitch += 1 * ds}, delta => motionDirectionViaMouse(delta))

        //PITCH NEGATIVE/DOWN
        doAction(control.triggerPitchNegative, _ => {pitch -= 1 * ds}, delta => motionDirectionViaMouse(delta))

        //YAW NEGATIVE/LEFT
        doAction(control.triggerYawLeft, _ => {yaw = (yaw + 1 * ds) % 360}, delta => motionDirectionViaMouse(delta))

        //YAW POSITIVE/RIGHT
        doAction(control.triggerYawRight, _ => {yaw = (yaw - 1 * ds) % 360}, delta => motionDirectionViaMouse(delta))

        //FIRE GUN
        doAction(control.triggerFire, _ => {gun.shoot(true)}, delta => {}, _ => gun.shoot(true))


        def motionDirectionViaMouse(mouseDeltaNew: Vec3f) {

          // calculate current mouse delta - considering velocities/sensitivities
          val deltaX: Float = (Input.mouseMovement.x * 0.3f) * ds
          val deltaY: Float = (Input.mouseMovement.y * 0.5f) * ds

          // sum deltas to get a pitch and yaw delta
          pitch += deltaY
          yaw -= deltaX



          // constrain pitch
          if (pitch > gun.pitchConstraintPositive )  pitch = gun.pitchConstraintPositive
          if (pitch < gun.pitchConstraintNegative) pitch = gun.pitchConstraintNegative

          //constrain yaw
          if (yaw < -(gun.yawConstraint)) yaw = -(gun.yawConstraint)
          if (yaw > gun.yawConstraint)   yaw = gun.yawConstraint

        }
          pos.rotation = Vec3f(pitch,yaw, pos.rotation.z)
          //println(pos.rotation)

      case _ =>

    }




  }
  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit, buttonAction: Unit => Unit = Unit => {}){
    Input.mouseMovementDo(triggers.mouseMovement, delta => mouseAction(delta))
    Input.mouseButtonDownDo(triggers.mouseButton, _ => buttonAction())
    Input.keyDownDo(triggers.key, _ => keyAction())
  }



}
