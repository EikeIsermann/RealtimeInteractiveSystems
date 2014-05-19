package main.scala.systems.positional

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.PositionalRootNode
import main.scala.components.{Placement, Children}
import main.scala.entities.Entity
import scala.collection.mutable.ListBuffer
import main.scala.tools.DC

/**
 * User: uni
 * Date: 19.05.14
 * Time: 08:04
 * This is a RIS Project class
 */
class RelativePositionalSystem extends ProcessingSystem{
  var node: Class[_ <: Node] = classOf[PositionalRootNode]

  var priority: Int = 4
  /**
   * called on system startup
   * @return
   */
  override def init(): System = {
    this
  }

  def begin(): Unit = {
    DC.log("RelativePositional run")

  }



  def processNode(node: Node): Unit = {

    val placement = node -> classOf[Placement]
    val children = node -> classOf[Children]
    val allChilds: ListBuffer[Entity] = new ListBuffer[Entity]
    for(e <- children.children){
      getChildren(e, allChilds)
    }
    for(e <- allChilds){
      //println("Processing " + e.identifier)

      if(e.has(classOf[Placement])){
       var p = e.getComponent(classOf[Placement])
       p.relativeUpdate(placement)
      }
    }
  }

  def getChildren(e: Entity, list: ListBuffer[Entity]): ListBuffer[Entity] = {
     if(e.has(classOf[Children])){
       for (c <- e.getComponent(classOf[Children]).children){
          getChildren(c, list)
       }
     }
    list.+=(e)
  }


  def end(): Unit = {}

  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}
}
