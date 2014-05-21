package main.scala.systems.input

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.{GunControlNode, DriveControlNode}
import main.scala.components.{Placement, Vehicle, DriveControl, Physics}
import main.scala.math.{RISMath, Vec3f}
import main.scala.io.Wavefront.Vec3

/**
 * User: uni
 * Date: 19.05.14
 * Time: 19:06
 * This is a RIS Project class
 */
class DriveControlSystem extends ProcessingSystem {
  var node: Class[_ <: Node] = classOf[DriveControlNode]
  var priority: Int = 1
  var boost: Float = 1
  var ds: Float = 1
  /**
   * called on system startup
   * @return
   */
  def init(): System = {this}

  /**
   * executed before nodes are processed - every update
   */
  def begin(): Unit = { boost = 1}

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
      case driveCon: DriveControlNode =>
        var phy = node -> classOf[Physics]
        var con = node -> classOf[DriveControl]
        var veh = node -> classOf[Vehicle]
        var pos = node -> classOf[Placement]


        // doAction ( TRIGGER , KEYBOARD ACTION, MOUSE ACTION, CONTROLLER ACTION .... )
        //FORWARD
        doAction(con.triggerBoost, _ => {
          boost = veh.boostFactor
          ds = 1 //ctx.deltaT * 100
        }, delta => {})

        doAction(con.triggerForward, _ => {



          phy.addForce(RISMath.DirFromRot(pos.rotation, true)*veh.power*boost)

        }, delta => {})

        //BACKWARD
        doAction(con.triggerBackward, _ => {

          phy.addForce(RISMath.DirFromRot(pos.rotation, false)*veh.power*boost )
        }, delta => {})

        //LEFT
        doAction(con.triggerLeft, _ => {
          pos.rotation = Vec3f(pos.rotation.x, pos.rotation.y + (veh.turnSpeed / boost ), pos.rotation.z)
        }, delta => {})

        //RIGHT
        doAction(con.triggerRight, _ => {
          pos.rotation = Vec3f(pos.rotation.x, pos.rotation.y - (veh.turnSpeed /boost ), pos.rotation.z)


        }, delta => {})

      case _ =>

    }
  }
  private def doAction(triggers: Triggers, keyAction: Unit => Unit, mouseAction: Vec3f => Unit){
    Input.mouseMovementDo(triggers.mouseMovement, delta => mouseAction(delta))
    Input.keyDownDo(triggers.key, _ => keyAction())
  }

}
