package main.scala.entities

import main.scala.architecture.Component
import main.scala.tools.Identifier

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
}
