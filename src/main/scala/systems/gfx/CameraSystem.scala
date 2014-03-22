package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.systems.input.SimulationContext
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.{CameraNode, RenderNode}


/**
 * Created by Eike on 22.03.14.
 */
class CameraSystem extends System {


  override def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(classOf[CameraNode])

    this
  }


  override def init(): System = {
    DC.log("Camera System initialized")
    this
  }
}
