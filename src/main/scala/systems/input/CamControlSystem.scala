package main.scala.systems.input

import main.scala.architecture._
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.CamControlNode
import main.scala.components.{CamControl, Motion}
import main.scala.math.Vec3f
import org.lwjgl.input.Mouse
import org.lwjgl.opengl.Display

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

  var pitchFac: Float = 0.0f
  var yawFac: Float = 0.0f

  var horizontalAngle: Float = 0.0f
  var verticalAngle: Float = 0.0f
  var dir: Vec3f = Vec3f()
  override def update(context: SimulationContext): System = {


    val nodes = GameEngine.getNodeList(new CamControlNode)
    for (node <- nodes) {
      val motion = node -> classOf[Motion]
      val control = node -> classOf[CamControl]


      val movementVelocity =  control.movementVelocity * context.deltaT
      val pitchVelocity =  control.pitchVelocity
      val yawVelocity =  control.yawVelocity




      // doAction ( TRIGGER , KEYBOARD ACTION, MOUSE ACTION, CONTROLLER ACTION .... )
      doAction(control.triggerForward, _ => {
        x += movementVelocity * math.sin(math.toRadians(-yaw)).toFloat
        z -= movementVelocity * math.cos(math.toRadians(-yaw)).toFloat
      }, (a,b,c) => {})
      doAction(control.triggerBackward, _ => {
        x -= movementVelocity * math.sin(math.toRadians(-yaw)).toFloat
        z += movementVelocity * math.cos(math.toRadians(-yaw)).toFloat

      }, (a,b,c) => {})
      doAction(control.triggerLeft, _ => {
        x -= movementVelocity * math.sin(math.toRadians(-yaw+90f)).toFloat
        z += movementVelocity * math.cos(math.toRadians(-yaw+90f)).toFloat
      }, (a,b,c) => {})
      doAction(control.triggerRight, _ => {
        x -= movementVelocity *  math.sin(math.toRadians(-yaw-90f)).toFloat
        z += movementVelocity * math.cos(math.toRadians(-yaw-90f)).toFloat
      }, (a,b,c) => {})

      doAction(control.triggerPitchPositive, _ => {pitch += 1}, (a,b,c) => motionDirectionViaMouse(a,b,c))
      doAction(control.triggerPitchNegative, _ => {pitch -= 1}, (a,b,c) => motionDirectionViaMouse(a,b,c))
      doAction(control.triggerYawLeft, _ => {yaw = (yaw+1)%360  }, (a,b,c) => motionDirectionViaMouse(a,b,c))
      doAction(control.triggerYawRight, _ => {yaw = (yaw-1)%360 }, (a,b,c) => motionDirectionViaMouse(a,b,c))


      def motionDirectionViaMouse(mouseInfos: (Vec3f, Vec3f, Vec3f)) {

        val (pos, posNorm, delta) = mouseInfos



        val newMpos: Vec3f = delta
        val nx = (newMpos.x-mpos.x)*pitchVelocity
        val ny = (newMpos.y-mpos.y)*yawVelocity


        pitchFac += ny
        yawFac += nx



        yaw -= yawFac

        pitch += pitchFac

        if(pitch > 90.0f) {
          pitch = 90.0f
        }
        if(pitch < -90.0f) {
          pitch = -90.0f
        }


        if (yaw < -180.0f)
        {
          yaw += 360.0f
        }

        if (yaw > 180.0f)
        {
          yaw -= 360.0f
        }

        println(pitch+"\t"+yaw)




        mpos = newMpos

        //motion.direction = Vec3f(mv.y*pitchVelocity,mv.x*yawVelocity,0)

        //Mouse.setCursorPosition(Display.getWidth/2, Display.getHeight/2)
      }


      //println(pitch, yaw)

      motion.velocity = Vec3f(x,y,z)
      motion.direction = Vec3f(pitch,yaw,0)



    }

    this
  }



  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: (Vec3f,Vec3f,Vec3f) => Unit) {

    Input.mousePositionNormalizedDo(triggers.mouseMovement, (v,c,d) => mouseAction(v,c,d))
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
