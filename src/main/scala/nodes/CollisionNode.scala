package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components._

/**
 * Created by Christian Treffs
 * Date: 07.05.14 15:53
 */
case class CollisionNode(collision: Collision, placement: Placement) extends Node(collision, placement) {
  var containsNot: List[Class[_ <: Component]] =  List()

}
