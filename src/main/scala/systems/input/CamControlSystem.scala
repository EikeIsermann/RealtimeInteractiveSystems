package main.scala.systems.input

import main.scala.architecture._
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.CamControlNode
import main.scala.components.{CamControl, Motion}
import main.scala.math.Vec3f
import org.lwjgl.input.Mouse

/**
 * Created by Eike
 * 23.03.14.
 */
class CamControlSystem extends System {


  var pitch: Float = 0
  var yaw: Float = 0
  var x: Float = 0
  val y: Float = 0
  var z: Float = 0
  val offsetX: Float = 0.1f
  val offsetZ: Float = 0.1f
  var mpos: Vec3f = Vec3f()

  override def update(context: SimulationContext): System = {


    val nodes = GameEngine.getNodeList(new CamControlNode)
    for (node <- nodes) {
      val motion = node -> classOf[Motion]
      val control = node -> classOf[CamControl]


      val movementVelocity =  control.movementVelocity * context.deltaT
      val pitchVelocity =  control.pitchVelocity * context.deltaT
      val yawVelocity =  control.yawVelocity * context.deltaT




      // doAction ( TRIGGER , KEYBOARD ACTION, MOUSE ACTION, CONTROLLER ACTION .... )
      doAction(control.triggerForward, _ => {
        x += movementVelocity * math.sin(math.toRadians(-yaw)).toFloat
        z -= movementVelocity * math.cos(math.toRadians(-yaw)).toFloat
      }, _ => {})
      doAction(control.triggerBackward, _ => {
        x -= movementVelocity * math.sin(math.toRadians(-yaw)).toFloat
        z += movementVelocity * math.cos(math.toRadians(-yaw)).toFloat

      }, _ => {})
      doAction(control.triggerLeft, _ => {
        x -= movementVelocity * math.sin(math.toRadians(-yaw+90f)).toFloat
        z += movementVelocity * math.cos(math.toRadians(-yaw+90f)).toFloat
      }, _ => {})
      doAction(control.triggerRight, _ => {
        x -= movementVelocity *  math.sin(math.toRadians(-yaw-90f)).toFloat
        z += movementVelocity * math.cos(math.toRadians(-yaw-90f)).toFloat
      }, _ => {})

      doAction(control.triggerPitchPositive, _ => {}, mv => motionDirectionViaMouse(mv))
      doAction(control.triggerPitchNegative, _ => {}, mv => motionDirectionViaMouse(mv))
      doAction(control.triggerYawLeft, _ => {yaw+=1}, mv => motionDirectionViaMouse(mv))
      doAction(control.triggerYawRight, _ => {yaw-=1}, mv => motionDirectionViaMouse(mv))


      def motionDirectionViaMouse(mouseNormalizePos: Vec3f) {
        val newMpos: Vec3f = mouseNormalizePos
        yaw -= (newMpos.x-mpos.x)*yawVelocity
        val newPitch = pitch - (newMpos.y-mpos.y)*pitchVelocity
        if(newPitch < 90 && newPitch > -90 ){
          pitch = newPitch
        }
        mpos = newMpos

        //motion.direction = Vec3f(mv.y*pitchVelocity,mv.x*yawVelocity,0)
      }




      motion.velocity = Vec3f(x.toFloat,y.toFloat,z.toFloat)
      motion.direction = Vec3f(pitch.toFloat,yaw.toFloat,0)


    }

    this
  }



  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit) {

    Input.mousePositionNormalizedDo(triggers.mouseMovement, vec => mouseAction(vec))
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
