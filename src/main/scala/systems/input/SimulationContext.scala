package main.scala.systems.input

import ogl.app.Input
import org.lwjgl.input.{Mouse, Keyboard}
import ogl.vecmathimp.FactoryDefault._
import main.scala.tools.DC
import main.scala.math.Mat4f
import main.scala.shader.Shader

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
