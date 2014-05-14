package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.systems.input.SimulationContext
import main.scala.tools.DC
import main.scala.engine.GameEngine
import main.scala.nodes.CameraNode
import main.scala.components.{Camera, Placement}
import main.scala.math.{Vec3f, Mat4f}


/**
 * Created by Eike on 22.03.14.
 */
class CameraSystem extends System {

  def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(new CameraNode)


    for(node <- nodes){
      val pos = node -> classOf[Placement]
      var cam = node -> classOf[Camera]
      val matPos = Mat4f.translation(pos.position)
      val pitch = pos.rotation.x
      val yaw = pos.rotation.y
      val matRot = Mat4f.rotation(Vec3f(1,0,0),pitch) *Mat4f.rotation(Vec3f(0,1,0),yaw)
      val viewMat = matRot * matPos
      context.setViewMatrix(viewMat.inverseRigid)
      DC.log("Camera is at: ", pos.position.inline)
    }

    this
  }


  def init(): System = {
    DC.log("Camera System","initialized",2)
    this
  }

  def deinit(): Unit = {
    DC.log("Camera System","ended",2)
  }

  override var node: Class[_ <: Node] = _

  override def update(): System = ???

  override def begin(): Unit = ???

  override def end(): Unit = ???

  override var priority: Int = _
}
