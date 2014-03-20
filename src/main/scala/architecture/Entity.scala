package main.scala.architecture

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:52
 */
trait Entity {

  private val _components: ArrayBuffer[Component] = ArrayBuffer.empty[Component]

  def components: Array[Component] = _components.toArray
  def components(componentType: Class[_ <: Component]): Array[Component] = components.filter(_.getClass.equals(componentType))

  def +=(component: Component): Entity = {
    _components += component
    this
  }
  def -=(component: Component): Entity = {
    _components -= component
    this
  }

}
