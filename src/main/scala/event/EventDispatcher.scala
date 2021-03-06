package main.scala.event

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import main.scala.architecture.{Component, Node, Family}
import main.scala.entities.Entity
import main.scala.tools.DC
import main.scala.components.Camera

/**
 * Created by Christian Treffs
 * Date: 28.03.14 12:10
 *
 * Event Dispatcher holds a map of subscribers to a specific event type
 * can dispatch an event - at every subscriber the method receive ist called with this event
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
    distinctReceivers.foreach(e => {
      e.receive(event)
    })
   }
}

/**
 * the event receiver - must implement the receive method that is called on event reception
 */
trait EventReceiver {
  def receive(event: Event): Unit
}

/**
 * all events
 */
trait Event
case class TestEvent() extends Event
case class NodeAdded(node: Node)(implicit val family: Family) extends Event

case class EntityCreated(ent: Entity) extends Event
case class EntityChanged(ent: Entity) extends Event
case class EntityRemoved(ent: Entity) extends Event
case class ComponentRemoved(ent: Entity, comp: Component) extends Event
case class ComponentAdded(ent: Entity, comp: Component) extends Event

case class CycleCam() extends Event
case class RemoveCam(cam: Camera) extends Event
case class ActivateCam(cam: Camera) extends Event

case class PlayerEnteredView(enemy: Entity) extends Event
case class PlayerLeftView() extends Event