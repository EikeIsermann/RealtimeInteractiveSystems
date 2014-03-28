package main.scala.event

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 28.03.14 14:09
 *
 * A Signal is essentially a mini-dispatcher specific to one event, with its own array of listeners.
 * A Signal gives an event a concrete membership in a class.
 * Listeners subscribe to real objects, not to string-based channels.
 * Event string constants are no longer needed.
 */

/**
 * an abstract class for creating a signal
 */
abstract class Signal {
  protected val subscribers: ArrayBuffer[SignalEntry]
  def += (func: Unit => Any, runOnce: Boolean = false): Signal
  def subscribe(func: Unit => Any, runOnce: Boolean = false): Signal = this.+=(func,runOnce)
  def -= (func: Unit => Any): Signal
  def unsubscribe(func: Unit => Any): Signal = this.-=(func)
  def unsubscribeAll() = subscribers.clear()
  def dispatch(): Signal
}

/**
 * a signal entry contains the function to be run on dispatch and a run once flag to determine if the subscriber
 * should be notified only once
 * @param func the function to execute on dispatch
 * @param runOnce the run once flag to determine if the subscriber should be notified only once and then be removed
 */
sealed case class SignalEntry(func: Unit => Any, runOnce: Boolean)

/**
 * Signal of type 01 - executes a function on dispatch
 */
case class Signal01() extends Signal {
  protected val subscribers: ArrayBuffer[SignalEntry] = ArrayBuffer.empty[SignalEntry]

  override def dispatch() = {
    subscribers.foreach(e => {
      e.func.apply() // apply the function
      if(e.runOnce) this.-=(e.func) // remove if once
    })
    this
  }

  override def +=(func: Unit => Any, runOnce: Boolean = false) = {
    subscribers += SignalEntry(func, runOnce)
    this
  }

  override def -=(func1: Unit => Any) = {
    subscribers --= subscribers.filter(e => e.func == func1)
    this
  }
}