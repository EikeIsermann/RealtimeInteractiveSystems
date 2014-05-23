package main.scala.systems.gfx
import main.scala.architecture._
import main.scala.tools.DC
import main.scala.nodes.CameraNode
import main.scala.math.{Mat4f, RISMath, Vec3f}
import main.scala.components.{Projectile, Camera, Placement}
import scala.collection.mutable.ListBuffer
import scala.collection.mutable
import main.scala.event._
import main.scala.event.CycleCam
import main.scala.engine.GameEngine


/**
 * Created by Eike on
 * 22.03.14.
 */
class CameraSystem extends ProcessingSystem with EventReceiver {

  override var node: Class[_ <: Node] = classOf[CameraNode]
  override var priority: Int = 0

  var activeCam: Seq[Camera] =   Seq[Camera]()

  EventDispatcher.subscribe(classOf[Event])(this)




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
        if(!activeCam.contains(cam)) {
          activeCam = activeCam.filterNot(e => GameEngine.entities.apply(e.owner.toString).has(classOf[Projectile]))
          activeCam = activeCam.:+(cam)
        }
        if(cam.active && cam != activeCam.head) cam.active = false
        if(cam.active && cam == activeCam.head){

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
        ctx.setCameraIsAt(pos.getMatrix)
        ctx.setViewMatrix(viewMat.inverseRigid)
        //DC.log("Camera is at: ", pos.position.inline)
        }
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
  def receive(event: Event): Unit = {
    event match {
      case cc: CycleCam => {
          if(activeCam.size > 1){
          activeCam = activeCam.tail.:+(activeCam.head)
           var ent = GameEngine.entities.apply(activeCam.last.owner.toString)
           if(ent.has(classOf[Projectile]))  {ent.remove(activeCam.last)}
          activeCam.head.active = true
          activeCam.last.active = false }
      }
      case rc: RemoveCam => {
        activeCam = activeCam.filterNot(c => c == rc.cam)
        if(!activeCam.isEmpty) activeCam.head.active = true
      }
      case ac: ActivateCam => {
        activeCam.head.active = false
        activeCam = activeCam.filterNot(c => c == ac.cam).+:(ac.cam)
        activeCam.head.active = true

      }
      case _ =>

    }

  }
}
