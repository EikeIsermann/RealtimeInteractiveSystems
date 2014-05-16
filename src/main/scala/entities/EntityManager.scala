package main.scala.entities

import main.scala.architecture.Component
import main.scala.tools.{DC, Identifier}
import main.scala.io.EntityTemplateLoader
import main.scala.components.{isPartOf, Children, Parent, hasParts}
import scala.xml.Elem
import main.scala.event.{ComponentRemoved, ComponentAdded, EntityCreated, EventDispatcher}
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:13
 */

object EntityManager {
  def create(name: String, template: Boolean = false): EntityManager = {
    if(template) DC.log("TEMPLATE: "+name,"created",3) else DC.log("INSTANCE OF: "+name,"created",3)
    new EntityManager(name,template)
  }
  def createWith(name: String, components: Component*): EntityManager = {
    val e = create(name)
    components.foreach(e.add)
    e
  }

  def newInstanceOf(name: Symbol): EntityManager  = {

    val templateEntity = EntityTemplateLoader.get(name)

    val newEntity: EntityManager = templateEntity.newInstance()
    val templateComp = templateEntity.components

    templateComp.foreach{
     case hP: hasParts =>
       // has children - so add them
        val children = new Children()
        children.owner = newEntity.identifier
       // for each part that is child of this entity
       hP.parts.foreach(part => {
         // create child
         val subEntity = EntityManager.newInstanceOf(part)
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

class EntityManager(name1: String, template: Boolean = false) {

  private val _components: ArrayBuffer[Component] = ArrayBuffer.empty[Component]

  def components: Array[Component] = _components.toArray
  def components(componentType: Class[_ <: Component]): Array[Component] = components.filter(_.getClass.equals(componentType))

  def add(component: Component): EntityManager = this.+=(component)
  def += (component: Option[Component]): EntityManager = {
    component.collect {
      case c: Component =>
        this.+=(c)
        c.owner = this.identifier
      case _ =>
    }
    this
  }
  def +=(component: Component): EntityManager = {
    _components.+=(component)
    EventDispatcher.dispatch(ComponentAdded(this,component))

    this
  }
  def remove(component: Component): EntityManager = this.-=(component)
  def -=(component: Component): EntityManager = {
    _components -= component

    EventDispatcher.dispatch(ComponentRemoved(this,component))

    this
  }

  def has(componentClass: Class[_ <: Component]): Boolean = {
    !components(componentClass).isEmpty
  }





  private val _identifier: Identifier = Identifier.create(name1)

  //dispatching entity creation event
  if(!template) EventDispatcher.dispatch(EntityCreated(this))


  def id: Long = _identifier.id
  def name: String = _identifier.name
  def identifier: Identifier = _identifier
  def equals(e: EntityManager): Boolean = this.==(e)
  def ==(e: EntityManager): Boolean = e.identifier == this.identifier
  override def toString: String = "[Entity] "+identifier.toString


//  override def clone()

  /*override def receive: Receive = {
    case m:Message => DC.log(this+" received", m)
  } */

  def newInstance(template: Boolean = false): EntityManager = EntityManager.create(name, template)
  def toXML: Elem = {
    <entity identifier={identifier.toString}>
      {components.map(_.toXML)}
    </entity>
  }
}
