package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.Health

/**
 * Created by Christian Treffs
 * Date: 22.05.14 14:35
 */
class HealthNode(health: Health) extends Node(health){

  def this() = this(new Health())
}
