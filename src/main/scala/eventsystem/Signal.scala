package main.scala.eventsystem


import akka.actor.ActorRef
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:35
 */
class Signal[T](private var value: T)(implicit val owner: ActorRef) {

  private val observers = new mutable.HashSet[ActorRef]()

  def now() = value

  def update(newValue: T)(implicit setterActor: ActorRef) {
    if(setterActor == owner) {
      // owner sets the new value -> notify observers
      value = newValue
      observers.foreach(_ ! SignalUpdatedValue(this, value))
    } else {
      // tell the owner to set the new value
      owner ! SignalUpdateValue(this, newValue)
    }
  }

  def registerObserver(actor: ActorRef) {
    if(actor != owner) {
      observers.add(actor)
    }
  }



}
