package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.tools.DC
import main.scala.nodes.CameraNode
import main.scala.math.{Vec3f, Mat4f}
import main.scala.components.{Camera, Placement}


/**
 * Created by Eike on
 * 22.03.14.
 */
class CameraSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[CameraNode]
  override var priority: Int = 0



  def init(): System = {
    this
  }

  def deinit(): Unit = {}

  override def begin(): Unit = {}

  override def processNode(n: Node): Unit = {
    n match {
      case camNode: CameraNode =>
        val pos: Placement = camNode -> classOf[Placement]
        val cam: Camera = camNode -> classOf[Camera]

        //set field of view
        ctx.fieldOfView = cam.fieldOfView
        ctx.aspect = cam.aspect
        ctx.farPlane = cam.farPlane
        ctx.nearPlane = cam.nearPlane

        val matPos = Mat4f.translation(pos.position)
        val pitch = pos.rotation.x
        val yaw = pos.rotation.y
        val matRot = Mat4f.rotation(Vec3f(1,0,0),pitch) * Mat4f.rotation(Vec3f(0,1,0),yaw)
        val viewMat = matRot * matPos

        // set camera pos
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
