package main.scala.systems.input

import main.scala.tools.DC
import main.scala.math.Mat4f
import main.scala.systems.gfx.Shader
import org.lwjgl.input.Keyboard

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

    Input.keyDown(Key.ArrowUp,println)
    Input.keyDown(Key.ArrowDown,println)
    Input.keyDown(Key.ArrowLeft,println)
    Input.keyDown(Key.ArrowRight,println)


    Input.keyDown(Key.Space,println)
    Input.keyDown(Key.BackSpace,println)
    Input.keyDown(Key.Enter,println)

    Input.keyDown(Key.ShiftLeft,println)
    Input.keyDown(Key.ShiftRight,println)

    Input.keyDown(Key.CommandRight,println)

    Input.keyDown(Key.CommandLeft,println)
    Input.keyDown(Key.AltLeft,println)
    Input.keyDown(Key.CtrlLeft,println)

    Input.keyDown(Key._W,println)
    Input.keyDown(Key._A,println)
    Input.keyDown(Key._S,println)
    Input.keyDown(Key._D,println)


    Input.mouseButtonDown(MouseButton.Left, println)
    Input.mouseButtonDown(MouseButton.Right,println)
    Input.mouseButtonDown(MouseButton.Middle,println)


    //println(Keyboard.getEventKey, Keyboard.getKeyName(Keyboard.getEventKey))


  }

}
