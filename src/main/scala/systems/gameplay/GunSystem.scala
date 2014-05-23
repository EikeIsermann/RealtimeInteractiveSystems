package main.scala.systems.gameplay

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.GunNode
import main.scala.components._
import main.scala.engine.GameEngine
import main.scala.entities.Entity
import main.scala.math.{RISMath, Mat4f, Vec3f}
import main.scala.components.Gun
import main.scala.event.{ActivateCam, EventDispatcher}

/**
 * User: uni
 * Date: 20.05.14
 * Time: 16:06
 * This is a RIS Project class
 */
class GunSystem extends ProcessingSystem {
  var node: Class[_ <: Node] = classOf[GunNode]
  var priority: Int = _

  /**
   * called on system startup
   * @return
   */
  def init(): System = {this}

  /**
   * executed before nodes are processed - every update
   */
  def begin(): Unit = {}

  /**
   * executed after nodes are processed - every update
   */
  def end(): Unit = {}

  /**
   * called on system shutdown
   */
  def deinit(): Unit = {}

  def processNode(node: Node): Unit = {
    node match{
      case gunnode: GunNode =>
        var gun = node -> classOf[Gun]
        var pos = node -> classOf[Placement]
        if(gun.timeOfLastShot + gun.coolDown < System.currentTimeMillis() && gun.shoot){
          var bullet = Entity.newInstanceOf(gun.projectile)
          bullet.getComponent(classOf[Placement]).setTo(pos)
          //bullet.getComponent(classOf[Placement]).scale = Vec3f(0.2f,0.2f,0.2f)
          var bullphys =  bullet.getComponent(classOf[Physics])
          bullphys.velocity = RISMath.DirFromRot(pos.basePosition.rotation*pos.rotation) * gun.power
          bullet.getIfPresent(classOf[Sound]).map(_.playList += 'tankFire)
          //val cam = new Camera(120f,None,0.1f,50f, true ,Vec3f(0,0,0),Vec3f(0,0,0), 0.1f )
          //bullet.add(cam)
          //EventDispatcher.dispatch(new ActivateCam(cam))
            //println(bullet.getIfPresent(classOf[Sound]))
          //bullphys.addForce(RISMath.DirFromRot(pos.rotation)*10000000)
           gun.shoot(false)
           gun.timeOfLastShot = System.currentTimeMillis()
        }
          gun.shoot(false)

      case _ =>
    }
  }
}
