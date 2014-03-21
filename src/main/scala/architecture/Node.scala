package main.scala.architecture

import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 20.03.14 20:51
 *
 * A Node is a combination of one or more Components.
 */
abstract class Node(args: Component*) {
  var components:  mutable.HashMap[Class[_ <: Component], Component] = new  mutable.HashMap[Class[_ <: Component], Component]
  for(comp <- args){
    components.put(comp.getClass, comp)
  }

  def -> [T <: Component](c: Class[T]): T = {
    components.apply(c).asInstanceOf[T]
  }
}
