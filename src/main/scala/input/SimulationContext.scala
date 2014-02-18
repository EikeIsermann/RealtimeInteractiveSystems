package main.scala.input

import ogl.app.Input
import org.lwjgl.input.{Mouse, Keyboard}
import ogl.vecmathimp.FactoryDefault._
import main.scala.tools.DC

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:17
 *
 * the entire game state
 */
class SimulationContext extends Context {

  DC.log("SimulationContext", this)

  private var _deltaT: Long = 0L

  def updateDeltaT(): Long = {

    _deltaT = 0L

    deltaT
  }

  def deltaT = _deltaT

  def reset() {  }

  def updateInput(input: Input){

    if(input.isKeyDown(Keyboard.KEY_W)) {
      println("W")
    }

    if(input.isKeyDown(Keyboard.KEY_A)) {
      println("A")
    }

    if(input.isKeyDown(Keyboard.KEY_S)) {
      println("S")
    }

    if(input.isKeyDown(Keyboard.KEY_D)) {
      println("D")
    }

  }

}
