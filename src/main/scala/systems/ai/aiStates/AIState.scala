package main.scala.systems.ai.aiStates

import main.scala.architecture.{Node, System}
import main.scala.event._
import main.scala.nodes.GunAINode
import main.scala.components.{GunAI, Gun, Placement}
import main.scala.entities.Entity
import main.scala.math.{Vec3f, RISMath}

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:23
 * This is a RIS Project class
 */
trait AIState extends EventReceiver {

  EventDispatcher.subscribe(classOf[Event])(this)

  def execute(node: Node)(implicit owner: System): AIState

  var nextState: AIState = null
}

class gunSearching() extends AIState{

  nextState = this
  def execute(node: Node)(implicit owner: System): AIState = {
         println("searching")
    nextState
  }

  override def receive(event: Event): Unit = {
    event match {
      case p: PlayerEnteredView => nextState = new gunTargetAcquired(p.enemy)
      case _ =>
    }
  }
}
class gunTargetAcquired(enemy: Entity) extends AIState {
  nextState = this    
  var e = enemy
  def execute(node: Node)(implicit owner: System): AIState = {
    node match {
      case gai: GunAINode  =>
        val pos = gai -> classOf[Placement]
        var gun = gai -> classOf[Gun]
        var gunai = gai -> classOf[GunAI]
        val target: Vec3f = e.getComponent(classOf[Placement]).getMatrix.position
        val vectorAim: Vec3f = ( target -pos.getMatrix.position ).norm
        val vectorNow: Vec3f = RISMath.DirFromRot(pos.getUnscaledMatrix.rotation * pos.rotation).norm






        val angle = math.acos((vectorAim ° vectorNow)/vectorAim.magnitude*vectorNow.magnitude)

        val cross: Vec3f = vectorAim % vectorNow

        val angleDeg: Float = math.toDegrees(angle).toFloat
        val factor: Float = 0.5f
        val positive: Float = if(cross.y < 0) factor  else -factor
        pos.rotation = Vec3f(pos.rotation.x, pos.rotation.y+(positive), pos.rotation.z)
        println(angleDeg)
        if(angleDeg < 10) nextState = new gunTargetLocked(e)







        //println(target, pos.getMatrix.getPosition, vectorAim.normalize(), vectorNow.normalize)




      case _ =>
    }
  nextState
  }

  override def receive(event: Event): Unit = {
     event match {
       case p: PlayerLeftView => nextState = new gunSearching()
       case _ =>
     }
  }
}

class gunTargetLocked(e: Entity) extends AIState {
  nextState = this
  def execute(node: Node)(implicit owner: System): AIState = {
    node match {
      case gn: GunAINode => {
        var gun = gn -> classOf[Gun]
        gun.shoot(true)
        nextState = new gunTargetAcquired(e)
      }
      case _ =>
    }
    nextState
  }

  override def receive(event: Event): Unit = {
    //TODO
  }
}

