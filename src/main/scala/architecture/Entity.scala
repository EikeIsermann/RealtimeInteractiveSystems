package main.scala.architecture

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:52
 *
 *
 * Entities are buckets that hold components
 */
trait Entity {

  private val _components: ArrayBuffer[Component] = ArrayBuffer.empty[Component]

  def components: Array[Component] = _components.toArray
  def components(componentType: Class[_ <: Component]): Array[Component] = components.filter(_.getClass.equals(componentType))

  def add(component: Component): Entity = this.+=(component)
  def +=(component: Component): Entity = {
     _components.+=(component)
    //TODO MESSAGE
    //GameEngine.actorRef ! ComponentAdded(this)
    this
  }
  def remove(component: Component): Entity = this.-=(component)
  def -=(component: Component): Entity = {
    _components -= component
    //TODO MESSAGE
    //GameEngine.actorRef ! ComponentRemoved(this)
    this
  }

  def has(componentClass: Class[_ <: Component]): Boolean = {
    !components(componentClass).isEmpty
  }




}
