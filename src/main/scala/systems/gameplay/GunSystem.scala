package main.scala.systems.gameplay

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.GunNode
import main.scala.components.{Physics, Gun, Placement}
import main.scala.engine.GameEngine
import main.scala.entities.Entity
import main.scala.math.{RISMath, Mat4f, Vec3f}

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

          //bullphys.addForce(RISMath.DirFromRot(pos.rotation)*10000000)
           gun.shoot(false)
           gun.timeOfLastShot = System.currentTimeMillis()
        }
          gun.shoot(false)

      case _ =>
    }
  }
}
