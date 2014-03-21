package main.scala.architecture

import scala.collection.mutable

/**
 * Created by Eike on 20.03.14.
 */
trait Family {

  protected var _entities: mutable.HashMap[Entity, Node]
  val nodeClass : Class[_ <: Node]
 protected val components: mutable.ListBuffer[Class[_ <: Component]]

  def entities = _entities

  def addIfMatch(entity: Entity){
    if (!_entities.contains(entity)){
      for(componentClass <- components){
        if(!entity.has(componentClass)){
          return
        }
      }
      var node = nodeClass.newInstance()
      for (componentClass <- components){
        node.components.put(componentClass, entity.components(componentClass).apply(0))
      }
      entities.put(entity,node)
    }
  }

  def componentRemoved(entity : Entity , componentClass : Class[_ <: Component] ) : Unit = {
    if(components.contains(componentClass) && entities.contains(entity)) entities.-(entity)
  }

 }
