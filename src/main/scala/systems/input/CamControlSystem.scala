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

      //z forward motion
      Input.keyDownDo(control.upKey, _ =>
        {
          motion.velocity += new Vec3f(0,0,movement)
        }
      )

      //z backward motion
      Input.keyDownDo(control.downKey, _ =>
      {
        motion.velocity -= new Vec3f(0,0,movement)
      }
      )
      //x forward motion
      Input.keyDownDo(control.rightKey, _ =>
      {
        motion.velocity += new Vec3f(movement,0,0)
      }
      )
      //x backward motion
      Input.keyDownDo(control.leftKey, _ =>
      {
        motion.velocity -= new Vec3f(movement,0,0)
      }
      )

    }

    this
  }
  override def init(): System = {
    DC.log("Control System","initialized",2)
    this
  }

  override def deinit(): Unit = {
    DC.log("Control System","ended",2)
  }
}
