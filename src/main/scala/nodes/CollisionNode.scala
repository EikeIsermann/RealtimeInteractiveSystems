package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components._

/**
 * Created by Christian Treffs
 * Date: 07.05.14 15:53
 */
case class CollisionNode(collision: Collision, placement: Placement) extends Node(collision, placement) {

  def contains: List[Class[_ <: Component]] =  List(classOf[Collision], classOf[Placement])
  def containsNot: List[Class[_ <: Component]] =  List()

}
