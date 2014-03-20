package main.scala.architecture

import main.scala.systems.input.Context
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:16
 */
trait Engine {

  private val _entities: ArrayBuffer[Entity] = ArrayBuffer.empty[Entity]
  private val _systems: ArrayBuffer[System] = ArrayBuffer.empty[System]


  def init(): Engine

  def mainLoop(): Unit

  def entities: Array[Entity] = _entities.toArray
  def systems: Array[System] = _systems.toArray

  def add(entity: Entity): Engine = this.+=(entity)
  def += (entity: Entity): Engine = {
    _entities += entity
    this
  }
  def remove(entity: Entity): Engine = this.-=(entity)
  def -= (entity: Entity): Engine = {
    _entities -= entity
    this
  }

  def add(system: System): Engine = this.+=(system)
  def += (system: System): Engine = {
    _systems += system
    this
  }
  def remove (system: System): Engine = this.-=(system)
  def -= (system: System): Engine = {
    _systems -= system
    this
  }

  def update(systemType: Class[_ <: System], context: Context): Engine = {
    _systems.filter(s => s.getClass.equals(systemType)).foreach(_.update(context))
    this
  }
  def update(context: Context): Engine = {
    _systems.foreach(_.update(context))
    this
  }
}
