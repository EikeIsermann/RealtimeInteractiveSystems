package main.scala.eventsystem

import scala.collection.mutable
import akka.actor.{ActorRef, Actor}


/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:36
 */
trait ObservingActor extends Actor {

  protected val activeSignals = mutable.HashMap[Signal[_], Any => Unit]()



  protected def observe[T](signal: Signal[T])(x: T => Unit) {
    activeSignals.put(signal, x.asInstanceOf[Any => Unit])
    signal.registerObserver(self)

  }

  final def receive: Actor.Receive = {

    case sig: SignalObserve[Any] => observe(sig.signal)(sig.func)

    case SignalUpdatedValue(signal, value) => activeSignals.get(signal).collect {
      case func: (Any => Unit)  => func.apply(value)
    }

    case msg: SignalUpdateValue => msg.signal.update(msg.value)

    case x => receiveElse(x)
  }

  protected def receiveElse (msg: Any)

  def registerObserverForSignal[T](signal: Signal[T], actor: ActorRef) {
    signal.registerObserver(actor)
  }


}



