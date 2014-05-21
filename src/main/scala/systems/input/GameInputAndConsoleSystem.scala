package main.scala.systems.input

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.GameConsoleNode
import main.scala.components.GameConsole
import main.scala.engine.GameEngine
import main.scala.systems.gfx.RenderingSystem
import main.scala.tools.{DC, DisplayManager}

/**
 * Created by Christian Treffs
 * Date: 21.05.14 22:19
 */
class GameInputAndConsoleSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[GameConsoleNode]
  override var priority: Int = 0

  private var consoleActive = false
  def toggleConsoleActive() {
    consoleActive = !consoleActive

    if(consoleActive) {
      DC.log("Console","activated",3)
      print("> ")
    } else {
      println()
      DC.log("Console","deactivated",3)
    }
  }
  def isConsoleActive: Boolean = consoleActive

  val systemsThatStayActive: Seq[Class[_ <: System]] =  Seq(classOf[GameInputAndConsoleSystem],classOf[RenderingSystem])



  /**
   * called on system startup
   * @return
   */
  override def init(): System = {

    this
  }

  /**
   * executed before nodes are processed - every update
   */
  override def begin(): Unit = {}


  override def processNode(node: Node): Unit = {

    node match {
      case gc: GameConsoleNode =>


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
      case _ =>
    }

  }




  def newLine() {
    println()
    print("> ")
  }

  def processKeys(gc: GameConsole) = {
    Input.keyDownOnceDo(Key.Enter, _ => {newLine();gc.processInput()})
    gc.validKeys.foreach(l => {
      Input.keyDownOnceDo(l,_ => gc.add(Key.asChar(l).toString))
    })
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
