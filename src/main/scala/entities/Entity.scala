package main.scala.entities

import main.scala.architecture.Component
import main.scala.tools.{DC, Identifier}
import main.scala.io.EntityTemplateLoader
import main.scala.architecture
import main.scala.components.{isPartOf, Children, Parent, hasParts}
import scala.xml.Elem

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:13
 */

object Entity {
  def create(name: String): Entity = new Entity(name)
  def createWith(name: String, components: Component*): Entity = {
    val e = create(name)
    components.foreach(e.add)
    e
  }

  def newInstanceOf(name: Symbol): Entity  = {
    val e = EntityTemplateLoader.get(name)

    val superEntity: Entity = e.newInstance()
    val parentComps = e.components

    parentComps.foreach{
     case hP: hasParts => {
       // has children - so add them
        val children = new Children()
       // for each part that is child of this entity
        hP.parts.foreach(part => {
          // create child
          val subEntity = Entity.newInstanceOf(part)
          // add this entity as parent
          subEntity += new Parent(superEntity)
          DC.log("Child entity added to parent entity",(subEntity,superEntity),1)
          // add each child to this entity as a child
          children += subEntity
        })

       // add all children
       superEntity += children
      }
     //do nothing - this can only be checked backwards
     // by doing nothing it is prevented, that the isPartOf component is not added to the real entity
     case iPO: isPartOf =>
       // add all other components as new instances
     case c: Component => {
       superEntity += c.newInstance()
       DC.log("Component added to entity",(c,superEntity),1)
     }
    }


    DC.log("Entity instance created",superEntity,1)
    superEntity

  }

}

class Entity(name1: String) extends main.scala.architecture.Entity {

  private val _identifier: Identifier = Identifier.create(name1)
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
  override def newInstance(): Entity = Entity.create(name)

  override def toXML: Elem = {
    <entity identifier={identifier.toString}>
      {components.map(_.toXML)}
    </entity>
  }
}
