package main.scala.systems.ai.aiStates

import main.scala.architecture.{Node, System}

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:23
 * This is a RIS Project class
 */
trait AIState {

  def execute(node: Node)(implicit owner: System): AIState

}

class gunSearching() extends AIState{
  def execute(node: Node)(implicit owner: System): AIState = {

    this
  }
}
class gunTargetAcquired() extends AIState {
  def execute(node: Node)(implicit owner: System): AIState = {
    this
  }
}

class gunTargetLocked() extends AIState {
  def execute(node: Node)(implicit owner: System): AIState = {
    this
  }
}

