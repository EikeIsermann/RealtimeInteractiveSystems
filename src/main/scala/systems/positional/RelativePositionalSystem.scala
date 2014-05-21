package main.scala.systems.positional

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.PositionalRootNode
import main.scala.components.{Placement, Children}
import main.scala.entities.Entity
import scala.collection.mutable.ListBuffer
import main.scala.tools.DC
import main.scala.math.Mat4f

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
    for(e <- children.children){
      updateChildren(e, placement.getMatrix , placement.getUnscaledMatrix)
    }

  }

  def updateChildren(e: Entity, m: Mat4f, unscaledMat: Mat4f): Boolean = {
    e.getComponent(classOf[Placement]).relativeUpdate(m,unscaledMat)
    if(e.has(classOf[Children])){
      val mat = e.getComponent(classOf[Placement]).getMatrix
      val unscaledMat = e.getComponent(classOf[Placement]).getUnscaledMatrix
       for (c <- e.getComponent(classOf[Children]).children){
         updateChildren(c, mat, unscaledMat)
       }
      false
     }
    true
  }


  def end(): Unit = {}

  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}
}
