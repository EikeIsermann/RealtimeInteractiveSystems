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
    //TODO: send add component message
    _components += component
    this
  }
  def remove(component: Component): Entity = this.-=(component)
  def -=(component: Component): Entity = {
    //TODO: send removed component message
    _components -= component
    this
  }

}
