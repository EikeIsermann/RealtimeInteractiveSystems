package main.scala.events


import akka.actor.ActorRef
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:35
 */
class Signal[T](private var value: T) {

  private val observers = new mutable.HashSet[ActorRef]()

  def update(newValue: T) = {
    value = newValue
    observers.foreach(_ ! UpdateSignalValue(this, value))
    value
  }

  def registerObserver(actor: ActorRef) {
    observers.add(actor)
  }


}
