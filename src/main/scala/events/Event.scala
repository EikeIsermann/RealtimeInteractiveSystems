package main.scala.events

/**
 * Created by Christian Treffs
 * Date: 12.11.13 16:35
 */


case class UpdateSignalValue[T](signal: Signal[T], value: T) extends Event

class Event {

}
