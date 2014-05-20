package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Placement, Gun}

/**
 * User: uni
 * Date: 20.05.14
 * Time: 17:30
 * This is a RIS Project class
 */
class GunNode(gun: Gun, pos: Placement) extends Node(gun,pos) {

 def this() = this(new Gun, new Placement)
}
