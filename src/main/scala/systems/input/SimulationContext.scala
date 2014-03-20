package main.scala.systems.input

import ogl.app.Input
import org.lwjgl.input.{Mouse, Keyboard}
import ogl.vecmathimp.FactoryDefault._
import main.scala.tools.DC
import main.scala.math.Mat4f
import main.scala.systems.gfx.Shader

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:17
 *
 * the entire game state
 */
class SimulationContext extends Context {

  var shader: Shader = null
  var modelMatrix: Mat4f = Mat4f.identity
  var projectionMatrix: Mat4f = Mat4f.identity
  var viewMatrix: Mat4f = Mat4f.identity

  DC.log("SimulationContext", this)

  private var _deltaT: Long = 0L

  def setProjectionMatrix(mat: Mat4f): Mat4f = {
    projectionMatrix = mat
    projectionMatrix
  }
  def setViewMatrix(mat: Mat4f): Mat4f = {
    viewMatrix = mat
    viewMatrix
  }

  def setModelMatrix(mat: Mat4f): Mat4f = {
    modelMatrix = mat
    modelMatrix
  }

  def updateDeltaT(): Long = {

    _deltaT = 0L

    deltaT
  }

  def deltaT = _deltaT

  def reset() {  }

  def updateInput(){

    if(Input.keyDown(Key.CommandLeft)) {
      println("Command/Win left")
    }
    if(Input.keyDown(Key.CommandRight)) {
      println("Command/Win right")
    }

    println(Keyboard.getKeyName(Keyboard.getEventKey))

    if(Input.keyDown(Key._W)) {
      println("W")
    }

    if(Input.keyDown(Key._A)) {
      println("A")
    }

    if(Input.keyDown(Key._S)) {
      println("S")
    }

    if(Input.keyDown(Key._D)) {
      println("D")
    }

  }

}
