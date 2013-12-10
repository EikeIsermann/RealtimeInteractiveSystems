package main.scala.eventsystem

import scala.collection.mutable
import akka.actor.{ActorRef, Actor}


/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:36
 */
trait ObservingActor extends Actor {

  /**
   * all active registered signals of the observing actor
   */
  protected val activeSignals = mutable.HashMap[Signal[_], Any => Unit]()


  /**
   * registers an observer for the given signal
   * @param signal the signal to be observed
   * @param func the function to be executed
   * @param observer the observing actor
   * @tparam T the type of signal
   * @return success true/false
   */
  private def observe[T](signal: Signal[T])(func: T => Unit)(observer: ActorRef) = {
    activeSignals.put(signal, func.asInstanceOf[Any => Unit])
    signal.registerObserver(observer)
  }

  /**
   * on receiving messages do this
   * @return ?
   */
  final def receive: Actor.Receive = {

    /**
     * register new observer for signal
     */
    case msg: ObserveSignal[Any] => observe(msg.signal)(msg.func)(msg.observer)

    /**
     * update signal value with yourself
     */
    case msg: UpdateSignalValue[_] => msg.signal.update(msg.value)(self)

    /**
     * publish signal changes to the observers
     */
    case PublishSignalValueUpdate(signal, value) => activeSignals.get(signal).collect {
      case func: (Any => Unit)  => func.apply(value)
      case _ => throw new IllegalArgumentException("can not apply function to the signal")
    }


    /**
     * handle unknown message
     */
    case unknownMsg => unknownMessage(unknownMsg)
    
  }

  protected def unknownMessage (msg: Any)

}



