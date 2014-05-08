package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components._

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:22
 */
case class PhysicsNode(physics: Physics, placement: Placement, motion: Motion) extends Node(physics,placement,motion)
