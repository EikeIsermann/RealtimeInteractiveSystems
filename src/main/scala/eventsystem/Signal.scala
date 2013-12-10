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

  def update[A <: T](newValue: A)(implicit setterActor: ActorRef) {
    setterActor match {
      case `owner` => updateObservers(newValue) // owner sets the new value -> notify observers
      case _       => updateSelf(newValue) // tell the owner to set the new value
    }
  }

  private def updateObservers(value: T) {
    observers.foreach(ref => ref ! SignalUpdatedValue[T](this, value))
  }

  private def updateSelf(value: T) {
    owner ! SignalUpdateValue[T](this, value)
  }

  def registerObserver(actor: ActorRef) {
    if(actor != owner) {
      observers.add(actor)
    }
  }



}
