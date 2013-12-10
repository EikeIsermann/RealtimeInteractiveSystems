package main.scala.entities

import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:22
 */
class EntityRegistry extends mutable.Set[Entity] {


  private val entityBuffer = mutable.ArrayBuffer[Entity]()
  
  def entities: Seq[Entity] = entityBuffer.toSeq

  def execute(f: Entity => Boolean): Boolean = entities.forall(f)
  
  def addEntity(e: Entity): this.type = {
    entityBuffer += e
    this
  }

  def removeEntity(e: Entity): this.type = {
    entityBuffer -= e
    this
  }

  def contains(elem: Entity): Boolean = entityBuffer.contains(elem)

  def iterator: Iterator[Entity] = entityBuffer.iterator

  def +=(elem: Entity): this.type = addEntity(elem)

  def -=(elem: Entity): this.type = removeEntity(elem)
}
