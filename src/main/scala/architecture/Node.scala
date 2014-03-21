package main.scala.architecture

import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 20.03.14 20:51
 *
 * A Node is a combination of one or more Components.
 */
trait Node {
  var components:  mutable.HashMap[Class[_ <: Component], Component]
}
