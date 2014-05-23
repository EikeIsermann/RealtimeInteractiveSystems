package main.scala.systems.ai.aiStates

import main.scala.architecture.{Node, System}
<<<<<<< 177d1532bb27f92190b06a1e22b59ecb5e8f4677
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

}

class gunSearching() extends AIState{
  var nextState: AIState = this
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
  class gunTargetAcquired() extends AIState {
  var e = GameEngine.entities.apply("Tank:1")
  def execute(node: Node)(implicit owner: System): AIState = {
    node match {
      case gai: GunAINode  =>
      var pos = gai -> classOf[Placement]
      var gun = gai -> classOf[Gun]
      var gunai = gai -> classOf[GunAI]
      var target = e.getComponent(classOf[Placement]).getMatrix.getPosition
      var vectorAim = pos.getMatrix.getPosition.sub(target)
      var vectorNow = RISMath.DirFromRot(pos.basePosition.rotation*pos.rotation)
      println(vectorAim, vectorNow)







      case _ =>
    }
    this
  }

  override def receive(event: Event): Unit = {
         //TODO
  }
}

class gunTargetLocked() extends AIState {
  def execute(node: Node)(implicit owner: System): AIState = {
    this
  }

  override def receive(event: Event): Unit = {
    //TODO
  }
}

