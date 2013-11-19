package main.scala.eventsystem

/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:35
 */


trait Message

case class TestMessage(msg: String) extends Message

case class SignalObserve[T](signal: Signal[T])(val func: T => Unit) extends Message
case class SignalUpdateValue[T](signal: Signal[T], value: T) extends Message
case class SignalUpdatedValue[T](signal: Signal[T], value: T) extends Message




