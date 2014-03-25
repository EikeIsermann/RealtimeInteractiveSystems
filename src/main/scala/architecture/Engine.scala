package main.scala.architecture

import main.scala.systems.input.{SimulationContext, Context}
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:16
 */

trait Engine {

  private val _entities:mutable.HashMap[Class[_ <: Entity], Entity] = new mutable.HashMap[Class[_ <: Entity], Entity]
  private val _systems: mutable.HashMap[Class[_ <: System], System] = new mutable.HashMap[Class[_ <: System], System]
  private val _families: mutable.HashMap[Class[_ <: Node], Family] = new mutable.HashMap[Class[_ <: Node], Family]



  def start(): Engine
  def createNewGame(title: String, assetsPath: String): Engine
  def shutdown(): Unit
  protected def gameLoop(): Unit


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
    system.deinit()
    this
  }
  def add (system: System): Engine = this.+=(system)
  def += (system: System): Engine = {
    _systems.put(system.getClass, system)
    system.init()
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

  def getNodeList(nodeClass : Node) : List[Node] = {
     families.get(nodeClass.getClass) match {
       case Some(fam) => fam.nodes.toList.asInstanceOf[List[Node]]
       case None =>
         val family: Family = new Family(nodeClass.getClass)
         families.put(nodeClass.getClass, family)
         for (entity <- entities.values){
           family.addIfMatch(entity)
         }
         family.nodes.toList.asInstanceOf[List[Node]]
     }
  }




  def update(context: SimulationContext): Engine = {
    systems.values.foreach(_.update(context))
    this
  }
}
