package main.scala.systems.ai.aiStates

import main.scala.architecture.{Node, System}
import main.scala.event._
import main.scala.entities.Entity

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
class gunTargetAcquired(enemy: Entity) extends AIState {
  def execute(node: Node)(implicit owner: System): AIState = {
    println("AQUIRING",enemy)
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

