package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.systems.input.SimulationContext
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.CameraNode
import main.scala.components.{Camera, Position}
import main.scala.math.Mat4f


/**
 * Created by Eike on 22.03.14.
 */
class CameraSystem extends System {


  override def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(new CameraNode)

    for(node <- nodes){
      var pos = node -> classOf[Position]
      var cam = node -> classOf[Camera]
      context.setViewMatrix(Mat4f.translation(pos.position))
      DC.log("Camera is at: ", pos.position.inline)
    }

    this
  }


  override def init(): System = {
    DC.log("Camera System","initialized",2)
    this
  }

  override def deinit(): Unit = {
    DC.log("Camera System","ended",2)
  }
}
