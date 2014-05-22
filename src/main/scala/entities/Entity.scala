package main.scala.entities

import main.scala.architecture.Component
import main.scala.tools.{DC, Identifier}
import main.scala.io.EntityTemplateLoader
import main.scala.components._
import scala.xml.Elem
import main.scala.event._
import scala.collection.mutable.ArrayBuffer
import main.scala.event.ComponentRemoved
import scala.Some
import main.scala.event.ComponentAdded
import main.scala.event.EntityCreated
import main.scala.event.ComponentRemoved
import main.scala.event.EntityRemoved
import scala.Some
import main.scala.event.ComponentAdded
import main.scala.event.EntityCreated

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:13
 */

object Entity {
  def create(name: String, template: Boolean = false): Entity = {
    if(template) DC.log("TEMPLATE: "+name,"created",3) else DC.log("INSTANCE OF: "+name,"created",3)
    new Entity(name,template)
  }
  def createWith(name: String, components: Component*): Entity = {
    val e = create(name)
    components.foreach(e.add)
    e
  }

  def newInstanceOf(name: Symbol): Entity  = {

    val templateEntity = EntityTemplateLoader.get(name)

    val newEntity: Entity = templateEntity.newInstance()
    val templateComp = templateEntity.components

    templateComp.foreach{
     case hP: hasParts =>
       // has children - so add them
        val children = new Children()
        children.owner = newEntity.identifier
       // for each part that is child of this entity
       hP.parts.foreach(part => {
         // create child
         val subEntity = Entity.newInstanceOf(part)
         // add this entity as parent
        val p1 = new Parent(newEntity)
         p1.owner = subEntity.identifier
         subEntity += p1
         DC.log("Child entity added to parent entity",(subEntity,newEntity),1)
         // add each child to this entity as a child
         children += subEntity
       })

       // add all children
       newEntity += children
     //do nothing - this can only be checked backwards
     // by doing nothing it is prevented, that the isPartOf component is not added to the real entity
     case iPO: isPartOf =>
       // add all other components as new instances
     case c: Component =>
       val c1 = c.newInstance()
       c1.owner = newEntity.identifier
       newEntity += c1
       DC.log("Component added to entity",(c.owner,c,newEntity),1)
    }



    DC.log("Entity instance created",newEntity,1)
    newEntity

  }

}

class Entity(idx: Identifier, template: Boolean = false) {

  private val _components: ArrayBuffer[Component] = ArrayBuffer.empty[Component]

  private val _identifier: Identifier = idx

  def this(name1: String, template1: Boolean) = this(Identifier.create(name1),template1)

  def components: Array[Component] = _components.toArray
  def components[T <: Component](componentType: Class[T]): Array[T] = components.filter(_.getClass.equals(componentType)).asInstanceOf[Array[T]]

  def add(component: Component): Entity = {println(this.identifier) ;  +=(component)}
  def += (component: Option[Component]): Entity = {
    component.collect {
      case c: Component =>
        this.+=(c)
      case _ =>
    }
    this
  }
  def +=(component: Component): Entity = {
    _components.+=(component)
    component.owner = identifier
    EventDispatcher.dispatch(ComponentAdded(this,component))

    this
  }
  def remove(component: Component): Entity = this.-=(component)
  def -=(component: Component): Entity = {
    _components -= component


    EventDispatcher.dispatch(ComponentRemoved(this,component))
    component match {
      case c: Camera => EventDispatcher.dispatch(new RemoveCam(c))
    }
    this
  }

  def has(componentClass: Class[_ <: Component]): Boolean = {
    !components(componentClass).isEmpty
  }

  def getComponent[T <: Component](c: Class[T]): T = {
   components(c).apply(0)
  }

  def getIfPresent[T <: Component](c: Class[T]): Option[T] = {
    has(c) match {
      case true   => Some(getComponent(c))
      case false  => None
    }
  }


  def kill() = destroy()
  def destroy() {
     // remove my components
    components.foreach(remove)

    // send remove myself from gameEngine event
    EventDispatcher.dispatch(EntityRemoved(this))
  }


  //dispatching entity creation event
  if(!template) EventDispatcher.dispatch(EntityCreated(this))


  def id: Long = _identifier.id
  def name: String = _identifier.name
  def identifier: Identifier = _identifier
  def equals(e: Entity): Boolean = this.==(e)
  def ==(e: Entity): Boolean = e.identifier == this.identifier
  override def toString: String = "[Entity] "+identifier.toString


//  override def clone()

  /*override def receive: Receive = {
    case m:Message => DC.log(this+" received", m)
  } */

  def newInstance(template: Boolean = false): Entity = Entity.create(name, template)
  def toXML: Elem = {
    <entity identifier={identifier.toString}>
      {components.map(_.toXML)}
    </entity>
  }
}
