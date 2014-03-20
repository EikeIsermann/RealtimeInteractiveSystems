package main.scala.world.entities

import scala.collection.mutable
import main.scala.tools.DC
import main.scala.systems.input.SimulationContext

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:22
 */
abstract class EntityRegistry extends mutable.Set[Entity] {


  private val entityBuffer = mutable.ArrayBuffer[Entity]()
  
  def entities: Seq[Entity] = entityBuffer.toSeq

  def initAll(context: SimulationContext)
  def simulateAll(context: SimulationContext)
  def renderAll(context: SimulationContext)
  
  def addEntity(e: Entity): this.type = {
    entityBuffer += e
    DC.log("Entity added", this)
    this
  }

  def removeEntity(e: Entity): this.type = {
    entityBuffer -= e
    DC.log("Entity removed", this)
    this
  }

  def contains(elem: Entity): Boolean = entityBuffer.contains(elem)

  def iterator: Iterator[Entity] = entityBuffer.iterator

  def +=(elem: Entity): this.type = addEntity(elem)

  def -=(elem: Entity): this.type = removeEntity(elem)
}
