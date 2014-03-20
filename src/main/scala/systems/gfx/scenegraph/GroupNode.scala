package main.scala.systems.gfx.scenegraph

import main.scala.systems.gfx.GFXComponent
import scala.collection.immutable.HashMap
import main.scala.math.Mat4f
import scala.collection.mutable
import main.scala.tools.DC
import main.scala.systems.gfx.scenegraph.visitor.Visitor

/**
 * User: uni
 * Date: 18.03.14
 * Time: 04:54
 * This is a RIS Project class
 */
class GroupNode(comp: GFXComponent) extends Node {

  private val objectSystem = new mutable.HashMap[Node, Mat4f]

  def getObjectSystem =  objectSystem

  def addNode(node: Node, trans:Mat4f) {
    if(!node.equals(this)){
      objectSystem.+=((node, trans))
    }
    else DC.warn("Nodes cannot reference themselves!")
  }

  def accept(v: Visitor){
    v.visitNode(this)
  }

}
