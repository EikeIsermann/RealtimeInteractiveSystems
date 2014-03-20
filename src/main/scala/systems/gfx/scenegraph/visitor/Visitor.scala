package main.scala.systems.gfx.scenegraph.visitor

import main.scala.systems.gfx.scenegraph.{LeafNode, GroupNode}

/**
 * Created by Eike on 20.03.14.
 */
trait Visitor {

  def enterNode(g: GroupNode)
  def leaveNode(g: GroupNode)
  def visitNode(g: GroupNode)
  def visitNode(l: LeafNode)
}
