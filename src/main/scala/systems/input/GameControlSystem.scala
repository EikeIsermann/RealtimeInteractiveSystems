package main.scala.systems.input

import main.scala.architecture.{VoidProcessingSystem, System, Node, ProcessingSystem}
import main.scala.nodes.GameConsoleNode
import main.scala.components.{Sound, Text}
import main.scala.engine.GameEngine
import main.scala.systems.gfx.{TextRenderingSystem, RenderingSystem}
import main.scala.tools.{DC, DisplayManager}
import main.scala.systems.gameplay.states.{Playing, GameState}
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 21.05.14 22:19
 */
class GameControlSystem extends VoidProcessingSystem {

  override var node: Class[_ <: Node] = classOf[GameConsoleNode]
  override var priority: Int = 0

  private var currentState: GameState = null

  /**
   * called on system startup
   * @return
   */
  override def init(): System = {
    currentState = new Playing()

    val sound = new Sound(mutable.HashMap('ost -> 'ost))
    sound.play('ost)
    this
  }

  /**
   * executed before nodes are processed - every update
   */
  override def begin(): Unit = {}


  override def processSystem(): Unit = {

    // execute the current state and set the new current state to the result of the execution
    currentState = currentState.execute()

    /*
    val gameConsole = gc -> classOf[GameConsole]
    Input.keyDownOnceDo(Key.GameConsole, _ => {gameConsole.clear(); toggleConsoleActive()})
    if(isConsoleActive) {
      GameEngine.pause(systemsThatStayActive)
      processKeys(gameConsole)
    } else {
      GameEngine.resume()

      //GLOBAL GAME INPUTS
      Input.keyDownOnceDo(Key._F, _ => DisplayManager.toggleFullscreen()) // FULLSCREEN
      Input.keyDownOnceDo(Key.Esc, _ => GameEngine.shutdown())
    }
    */
  }



  /**
   * executed after nodes are processed - every update
   */
  override def end(): Unit = {}
  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}


}
