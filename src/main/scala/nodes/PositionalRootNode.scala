package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Children, Placement}

/**
 * User: uni
 * Date: 19.05.14
 * Time: 08:06
 * This is a RIS Project class
 */
class PositionalRootNode(childs: Children, place: Placement) extends Node(childs, place) {
  def this() = this(new Children(), new Placement())

}
