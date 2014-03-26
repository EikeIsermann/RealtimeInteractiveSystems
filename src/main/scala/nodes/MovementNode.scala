package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Position, Motion}
import main.scala.math.Vec3f

/**
 * User: uni
 * Date: 25.03.14
 * Time: 11:34
 * This is a RIS Project class
 */
class MovementNode(motion: Motion, position:Position) extends Node{

  def this() = this(
    new Motion(),
    new Position(Vec3f(0,0,0), Vec3f(0,0,0))
  )

}
