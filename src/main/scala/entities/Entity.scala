package main.scala.entities

import main.scala.components.Position
import main.scala.architecture.Component

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:13
 */

object Entity {
  def create: Entity = new Entity
  def createWith(components: Component*): Entity = {
    val e = new Entity
    components.foreach(e.add)
    e
  }
}

class Entity extends main.scala.architecture.Entity {

}
