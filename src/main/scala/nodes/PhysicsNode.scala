package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components._

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:22
 */
class PhysicsNode(placement: Placement, motion: Physics) extends Node(placement,motion) {

  def this() = this(new Placement(), new Physics())

}
