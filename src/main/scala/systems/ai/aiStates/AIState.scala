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
  def execute(node: Node)(implicit owner: System): AIState = {
    println("AQUIRING",enemy)
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

