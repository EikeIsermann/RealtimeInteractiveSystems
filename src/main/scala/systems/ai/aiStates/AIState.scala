package main.scala.systems.ai.aiStates

import main.scala.architecture.{Node, System}
import main.scala.event._
import main.scala.entities.Entity
import main.scala.nodes.GunAINode
import main.scala.components.{GunAI, Gun, Placement}
import main.scala.entities.Entity
import main.scala.math.RISMath
import main.scala.engine.GameEngine

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
      var pos = gai -> classOf[Placement]
      var gun = gai -> classOf[Gun]
      var gunai = gai -> classOf[GunAI]
      var target = e.getComponent(classOf[Placement]).getMatrix.getPosition
      var vectorAim = target.sub(pos.getMatrix.getPosition)
      var vectorNow = RISMath.DirFromRot(pos.getUnscaledMatrix.rotation * pos.rotation)
      println(target, pos.getMatrix.getPosition, vectorAim.normalize(), vectorNow.normalize)







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

class gunTargetLocked() extends AIState {
  nextState = this
  def execute(node: Node)(implicit owner: System): AIState = {
    nextState
  }

  override def receive(event: Event): Unit = {
    //TODO
  }
}

