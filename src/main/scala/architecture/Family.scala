package main.scala.architecture

import scala.collection.mutable
import main.scala.tools.DC

/**
 * Created by Eike on 20.03.14.
 *
 * A collection of Entities with a set of Components
 */
class Family(val nodeClass: Class[_ <: Node]) {

 protected var _entities: mutable.HashMap[Entity, Node] = new mutable.HashMap[Entity, Node]
 var components: mutable.ListBuffer[Class[_ <: Component]] = new mutable.ListBuffer[Class[_ <: Component]]

  def entities = _entities
  def nodes = _entities.values

  def addIfMatch(entity: Entity){
    if (!_entities.contains(entity)){

      for(unwanted <- nodeClass.newInstance().containsNot){
        if(entity.has(unwanted)) {
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