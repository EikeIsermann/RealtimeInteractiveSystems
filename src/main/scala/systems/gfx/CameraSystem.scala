package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.systems.input.SimulationContext
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.{CameraNode}
import main.scala.components.{Camera, Position}
import main.scala.math.{Vec3f, Mat4f}


/**
 * Created by Eike on 22.03.14.
 */
class CameraSystem extends System {


  override def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(classOf[CameraNode])

    for(node <- nodes){
      var pos = node -> classOf[Position]
      var cam = node -> classOf[Camera]
      context.setViewMatrix(Mat4f.translation(pos.position).mult(Mat4f.rotation(0,1,0,pos.rotation.y())))
    }

    this
  }


  override def init(): System = {
    DC.log("Camera System initialized")
    this
  }
}
