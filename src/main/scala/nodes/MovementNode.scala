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
case class MovementNode(motion: Motion, placement:Placement) extends Node(motion, placement){

  def this() = this(
    new Motion(),
    new Placement(Vec3f(0,0,0), Vec3f(0,0,0))
  )

}
