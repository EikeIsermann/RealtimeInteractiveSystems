package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.tools.DC
import main.scala.nodes.CameraNode
import main.scala.math.{Vec3f, Mat4f}


/**
 * Created by Eike on
 * 22.03.14.
 */
class CameraSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[CameraNode]
  override var priority: Int = 0



  def init(): System = {
    DC.log("Camera System","initialized",2)
    this
  }

  def deinit(): Unit = {
    DC.log("Camera System","ended",2)
  }

  override def begin(): Unit = {}

  override def processNode(n: Node): Unit = {
    n match {
      case camNode: CameraNode =>
        val pos = camNode.placement
        var cam = camNode.camera //TODO

        val matPos = Mat4f.translation(pos.position)
        val pitch = pos.rotation.x
        val yaw = pos.rotation.y
        val matRot = Mat4f.rotation(Vec3f(1,0,0),pitch) *Mat4f.rotation(Vec3f(0,1,0),yaw)
        val viewMat = matRot * matPos
        ctx.setViewMatrix(viewMat.inverseRigid)
        //DC.log("Camera is at: ", pos.position.inline)
      case _ =>
    }
  }



  override def end(): Unit = {}


  /*
  def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(new CameraNode)


    for(node <- nodes){

    }

    this
  }

   */
}
