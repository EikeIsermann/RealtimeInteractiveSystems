package main.scala.core.events

import akka.actor.ActorRef

/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:35
 */


trait Message

case class TestMessage(msg: String) extends Message

/**
 * tell the observer-actor to observe the given signal  
 * @param signal the signal to be observed
 * @param func the function to be executed
 * @param observer the observer
 * @tparam T the type of the signal
 */
case class ObserveSignal[T](signal: Signal[T])(val func: T => Any)(implicit val observer: ActorRef) extends Message


/**
 * update the value of the given signal
 * @param signal the signal
 * @param value the new value
 * @tparam T the type of the signal
 */
case class UpdateSignalValue[T](signal: Signal[T], value: T) extends Message


/**
 * publish the the value change of the given signal
 * @param signal the signal
 * @param value the new value
 * @tparam T the type of the signal
 */
case class PublishSignalValueUpdate[T](signal: Signal[T], value: T) extends Message




