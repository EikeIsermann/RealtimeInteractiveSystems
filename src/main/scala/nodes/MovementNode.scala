package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components.{Placement, Motion}
import main.scala.math.Vec3f

/**
 * User: uni
 * Date: 25.03.14
 * Time: 11:34
 * This is a RIS Project class
 */
class MovementNode(motion: Motion, position:Placement) extends Node(motion, position){

  def this() = this(
    new Motion(),
    new Placement(Vec3f(0,0,0), Vec3f(0,0,0))
  )

  var containsNot: List[Class[_ <: Component]] = List()

  override def contains: List[Class[_ <: Component]] = ???
}
