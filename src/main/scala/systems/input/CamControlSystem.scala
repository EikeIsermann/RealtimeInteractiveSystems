package main.scala.systems.input
import main.scala.architecture._
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.CamControlNode
import main.scala.components.{CamControl, Motion}
import main.scala.math.Vec3f

/**
 * Created by Eike on 23.03.14.
 */
class CamControlSystem extends System {


  override def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(new CamControlNode)
    for(node <- nodes){
      val motion = node -> classOf[Motion]
      val control = node -> classOf[CamControl]


      val movement = context.deltaT*control.velocity



      // doAction ( TRIGGER , KEYBOARD ACTION, MOUSE ACTION, CONTROLLER ACTION .... )
      doAction(control.triggerForward,  _ => { motion.velocity += new Vec3f(0,0,movement)}, _ => {} )
      doAction(control.triggerBackward, _ => { motion.velocity -= new Vec3f(0,0,movement)}, _ => {} )
      doAction(control.triggerLeft,     _ => { motion.velocity += new Vec3f(movement,0,0)}, _ => {} )
      doAction(control.triggerRight,    _ => { motion.velocity -= new Vec3f(movement,0,0)}, _ => {} )

      doAction(control.triggerPitchPositive,     _ => {}, mv => { } )
      doAction(control.triggerPitchNegative,     _ => {}, _ => {} )
      doAction(control.triggerYawLeft,     _ => {}, _ => {} )
      doAction(control.triggerYawRight,     _ => {}, _ => {} )


    }

    this
  }

  private def doAction(triggers: Seq[Enumeration#Value], keyAction: Unit => Unit, mouseAction: Vec3f => Unit) {
    for(t <- triggers) {

      t match {
        case mv: MouseMovement.Value => {

          Input.mouseMovementDo(mv, vec => mouseAction(vec))
        }
        case key: Key.Value => {

          println("HALLO")
          Input.keyDownDo(key, _ => keyAction())
        }
        case _ => throw new IllegalArgumentException("can not recognize trigger " + t)
      }
    }
  }

  override def init(): System = {
    DC.log("Control System","initialized",2)
    this
  }

  override def deinit(): Unit = {
    DC.log("Control System","ended",2)
  }
}
