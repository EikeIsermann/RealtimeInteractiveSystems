package main.scala.systems.gfx.scenegraph

import java.util.UUID
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 05.11.13 17:00
 */
abstract class Node() {
  val id = UUID.randomUUID()
  val children = mutable.HashMap.empty[Edge, Node]
  val parents = mutable.HashMap.empty[Edge, Node]

  def connectChild(viaEdge: Edge, childNode: Node) = children.put(viaEdge, childNode)
  def connectParent(viaEdge: Edge, parentNode: Node) = parents.put(viaEdge, parentNode)

}
