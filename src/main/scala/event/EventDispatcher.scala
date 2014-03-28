package main.scala.event

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 28.03.14 12:10
 */
object EventDispatcher {

  private val eventReceivers = mutable.HashMap.empty[Class[_ <: Event], ArrayBuffer[EventReceiver]]


  def subscribe(eventInterestedIn: Class[_ <: Event])(implicit eventReceiver: EventReceiver) {
    if(!eventReceivers.contains(eventInterestedIn)) {
      eventReceivers.put(eventInterestedIn, new ArrayBuffer[EventReceiver]())
    }
    eventReceivers(eventInterestedIn) += eventReceiver
  }

  def unsubscribe(eventInterestedIn: Class[_ <: Event])(implicit eventReceiver: EventReceiver): Boolean = {
     if(eventReceivers.contains(eventInterestedIn)) {
       eventReceivers(eventInterestedIn) -= eventReceiver
        true
     }
    false
  }

  def dispatch(event: Event){
    val distinctReceivers = eventReceivers.filterKeys(_.isAssignableFrom(event.getClass)).values.flatten.toSeq.distinct
    distinctReceivers.foreach(_.receive(event))
   }
}

trait EventReceiver {
  def receive(event: Event)
}


trait Event
case class TestEvent() extends Event

