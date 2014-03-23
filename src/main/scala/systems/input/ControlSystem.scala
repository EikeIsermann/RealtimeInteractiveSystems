package main.scala.systems.input
import main.scala.architecture._
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.ControlNode

/**
 * Created by Eike on 23.03.14.
 */
class ControlSystem extends System {
  override def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(classOf[ControlNode])

    for(node <- nodes){


    }

    this
  }
  override def init(): System = {
    DC.log("Control System initialized")
    this
  }
}
