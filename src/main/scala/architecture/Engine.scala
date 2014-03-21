package main.scala.architecture

import main.scala.systems.input.Context
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable
import akka.actor.{ActorSystem, Actor}
import akka.actor.Actor.Receive
import main.scala.event.ComponentAdded
import scala.swing.event.ComponentRemoved

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:16
 */

trait Engine {

  private val _entities:mutable.HashMap[Class[_ <: Entity], Entity] = new mutable.HashMap[Class[_ <: Entity], Entity]
  private val _systems: mutable.HashMap[Class[_ <: System], System] = new mutable.HashMap[Class[_ <: System], System]
  private val _families: mutable.HashMap[Class[_ <: Node], Family] = new mutable.HashMap[Class[_ <: Node], Family]


  def init(): Engine
  def mainLoop(): Unit

  def entities:  mutable.HashMap[Class[_ <: Entity], Entity] = _entities
  def systems: mutable.HashMap[Class[_ <: System], System] = _systems
  def families: mutable.HashMap[Class[_ <: Node], Family] = _families

  def remove (entity: Entity): Engine = this.-=(entity)
  def -= (entity: Entity): Engine = {
    _entities - entity.getClass
    this
  }
  def add (entity: Entity): Engine = this.+=(entity)
  def += (entity: Entity): Engine = {
    _entities.put(entity.getClass, entity)
    this
  }
  def remove (system: System): Engine = this.-=(system)
  def -= (system: System): Engine = {
    _systems - system.getClass
    this
  }
  def add (system: System): Engine = this.+=(system)
  def += (system: System): Engine = {
    _systems.put(system.getClass, system)
    this
  }


  def remove (family: Family): Engine = this.-=(family)
  def -= (family: Family): Engine = {
    _families - family.nodeClass
    this
  }
  def add (family: Family): Engine = this.+=(family)
  def += (family: Family): Engine = {
    _families.put(family.nodeClass, family)
    this
  }


  def getSystem(systemClass : Class[_ <: System]) = systems.apply(systemClass)

  def componentAdded(entity: Entity){
    for(family <- families.values) family.addIfMatch(entity)
  }

  def getNodeList(nodeClass : Class[_ <: Node]) : List[Node] = {
     families.get(nodeClass) match {
       case Some(fam) => fam.nodes.toList
       case None => {
         var family : Family = new Family(nodeClass)
         families.put(nodeClass, family)
         for (entity <- entities.values){
           family.addIfMatch(entity)
         }
         return family.nodes.toList
       }
     }
  }




  def update(systemType: Class[_ <: System], context: Context): Engine = {
    systems.values.filter(s => s.getClass.equals(systemType)).foreach(_.update(context))
    this
  }
  def update(context: Context): Engine = {

    systems.values.foreach(_.update(context))
    this
  }
}
