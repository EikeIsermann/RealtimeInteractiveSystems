package main.scala.events

import scala.collection.mutable
import akka.actor.Actor


/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:36
 */
trait Observing extends Actor {

  protected val activeSignals = mutable.HashMap[Signal[_], Any => Unit]()



  protected def observe[T](signal: Signal[T])(x: T => Unit) {
    activeSignals.put(signal, x.asInstanceOf[Any => Unit])
    signal.registerObserver(self)

  }

  def receive: Actor.Receive = {
    case UpdateSignalValue(signal, value) => activeSignals.get(signal).collect {
      case func: (Any => Unit)  => func.apply(value)
    }
  }
}



