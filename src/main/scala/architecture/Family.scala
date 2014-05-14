package main.scala.architecture

import scala.collection.mutable
import main.scala.tools.DC
import main.scala.event.{NodeAdded, EventDispatcher}

/**
 * Created by Eike on 20.03.14.
 *
 * A collection of Entities with a set of Components
 */
class Family(val nodeClass: Class[_ <: Node]) {

 var _entities: mutable.HashMap[Entity, Node] = mutable.HashMap.empty[Entity, Node]
 implicit val family = this
 val components: List[Class[_ <: Component]] = nodeClass.newInstance().contains
 val unwanted: List[Class[_ <: Component]] = nodeClass.newInstance().containsNot

  def entities = _entities
  def nodes = _entities.values

  def addIfMatch(entity: Entity){
    if (!_entities.contains(entity)){

      for(componentClass <- unwanted){
        if(entity.has(componentClass)) {
        return
        }
      }

      for(componentClass <- components){
        if(!entity.has(componentClass)){
          return
        }
      }

      val node = nodeClass.newInstance()
      for (componentClass <- components){
        node.components.put(componentClass, entity.components(componentClass).apply(0))
      }
      entities.put(entity,node)
      EventDispatcher.dispatch(NodeAdded(node))
      DC.log("Family added entity ",(entity,node))
    }
  }

  def componentRemoved(entity : Entity , componentClass : Class[_ <: Component] ) : Unit = {
    if(components.contains(componentClass) && entities.contains(entity)) entities.remove(entity)
}

  def remove(entity: Entity){
    entities.remove(entity)
  }

}