package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.LifeTime

/**
 * Created by Christian Treffs
 * Date: 23.05.14 09:15
 */
class LifeTimeNode(lifeTime: LifeTime) extends Node(lifeTime){

  def this() = this(new LifeTime())
}
