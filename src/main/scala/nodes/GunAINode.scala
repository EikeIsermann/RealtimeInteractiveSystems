package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{GunAI, Placement, Gun}

/**
 * User: uni
 * Date: 23.05.14
 * Time: 01:48
 * This is a RIS Project class
 */
class GunAINode(gunAI: GunAI, gun: Gun, placement: Placement) extends Node(gunAI,gun,placement) {

  def this() = this(new GunAI(),new Gun(),new Placement())
}

