package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Text, Placement}

/**
 * Created by Christian Treffs
 * Date: 22.05.14 09:17
 */
class TextNode(gameConsole: Text, placement: Placement) extends Node(placement, gameConsole){

  def this() = this(new Text(),new Placement())
}
