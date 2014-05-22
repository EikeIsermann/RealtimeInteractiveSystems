package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{GameConsole, Placement}

/**
 * Created by Christian Treffs
 * Date: 22.05.14 09:17
 */
class TextNode(gameConsole: GameConsole, placement: Placement) extends Node(placement, gameConsole){

  def this() = this(new GameConsole(),new Placement())
}
