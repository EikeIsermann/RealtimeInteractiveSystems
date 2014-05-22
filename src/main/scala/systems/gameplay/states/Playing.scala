package main.scala.systems.gameplay.states

import main.scala.systems.input.{Key, Input}
import main.scala.engine.GameEngine
import main.scala.tools.DisplayManager
import main.scala.architecture.System

/**
 * Created by Christian Treffs
 * Date: 22.05.14 11:45
 *
 * The State when the game is playing normally
 */
class Playing extends GameState{
  override def execute()(implicit owner: System): GameState = {

    // needs to resume all systems that where potentially halted
    GameEngine.resume()

    //GLOBAL GAME INPUTS
    Input.keyDownOnceDo(Key._F,   _ => DisplayManager.toggleFullscreen()) // FULLSCREEN toggle
    Input.keyDownOnceDo(Key.Esc,  _ => GameEngine.shutdown()) // engine shutdown


    Input.keyDownOnceDo(Key.GameConsole, _ => return new InGameConsole)

    this
  }
}
