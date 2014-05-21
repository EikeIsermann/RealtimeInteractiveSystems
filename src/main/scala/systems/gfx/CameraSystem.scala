package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.tools.DC
import main.scala.nodes.CameraNode
import main.scala.math.{Mat4f, RISMath, Vec3f}
import main.scala.components.{Camera, Placement}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable


/**
 * Created by Eike on
 * 22.03.14.
 */
class CameraSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[CameraNode]
  override var priority: Int = 0

  var activeCam: Seq[Camera] =   Seq[Camera]()


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

        //if(!activeCam.contains(cam)) activeCam.+:(cam)
        //set field of view
        ctx.fieldOfView = cam.fieldOfView
        ctx.aspect = cam.aspect
        ctx.farPlane = cam.farPlane
        ctx.nearPlane = cam.nearPlane

        val matPos = Mat4f.translation(pos.position)
        val pitch = pos.rotation.x
        val yaw = pos.rotation.y
        //val matRot = Mat4f.rotation(Vec3f(1,0,0),pitch) * Mat4f.rotation(Vec3f(0,1,0),yaw) * pos.basePosition.rotation
         val matRot = pos.getMatrix.rotation
        //val viewMat = matRot * matPos

         var viewMat =  Mat4f.rotation(cam.offSetRot) * Mat4f.translation((RISMath.DirFromRot(cam.offSetRot,false)*cam.offSetDistance))
         viewMat = viewMat * pos.getUnscaledMatrix.getRotation * Mat4f.translation(pos.getMatrix.position)
        // set camera pos
        ctx.setViewMatrix(viewMat.inverseRigid)
        //DC.log("Camera is at: ", pos.position.inline)
      case _ =>
    }
  }


  //def activateCam(cam:Camera)

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
