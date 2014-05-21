package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.GameConsole

/**
 * Created by Christian Treffs
 * Date: 21.05.14 22:25
 */
class GameConsoleNode(consoleComp: GameConsole) extends Node(consoleComp){

  def this() = this(new GameConsole())
}
