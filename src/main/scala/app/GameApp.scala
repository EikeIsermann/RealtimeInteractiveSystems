package main.scala.app

import main.scala.engine.GameEngine

/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:39
 */

object GameApp {
  def main(args: Array[String]) {

    val game = GameEngine.createNewGame("RIS Game")
    game.start()

  }
}

