package main.scala.systems.input

import main.scala.architecture._
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.CamControlNode
import main.scala.components.{CamControl, Motion}
import main.scala.math.Vec3f

/**
 * Created by Eike
 * 23.03.14.
 */
class CamControlSystem extends System {


  override def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(new CamControlNode)
    for (node <- nodes) {
      val motion = node -> classOf[Motion]
      val control = node -> classOf[CamControl]


      val movementVelocity = context.deltaT * control.movementVelocity

      val pitchVelocity = control.pitchVelocity
      val yawVelocity = control.yawVelocity



      // doAction ( TRIGGER , KEYBOARD ACTION, MOUSE ACTION, CONTROLLER ACTION .... )
      doAction(control.triggerForward, _ => {motion.velocity += new Vec3f(0, 0, movementVelocity)}, _ => {})
      doAction(control.triggerBackward, _ => {motion.velocity -= new Vec3f(0, 0, movementVelocity)}, _ => {})
      doAction(control.triggerLeft, _ => {motion.velocity += new Vec3f(movementVelocity, 0, 0)}, _ => {})
      doAction(control.triggerRight, _ => {motion.velocity -= new Vec3f(movementVelocity, 0, 0)}, _ => {})

      doAction(control.triggerPitchPositive, _ => {}, mv => motionDirectionViaMouse(mv))
      doAction(control.triggerPitchNegative, _ => {}, mv => motionDirectionViaMouse(mv))
      doAction(control.triggerYawLeft, _ => {}, mv => motionDirectionViaMouse(mv))
      doAction(control.triggerYawRight, _ => {}, mv => motionDirectionViaMouse(mv))


      def motionDirectionViaMouse(mv: Vec3f) {
        motion.direction = Vec3f(mv.y*pitchVelocity,mv.x*yawVelocity,0)
      }
    }

    this
  }



  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit) {

    Input.mouseMovementDo(triggers.mouseMovement, vec => mouseAction(vec))
    Input.keyDownDo(triggers.key, _ => keyAction())

  }

  override def init(): System = {
    DC.log("Control System", "initialized", 2)
    this
  }

  override def deinit(): Unit = {
    DC.log("Control System", "ended", 2)
  }
}
