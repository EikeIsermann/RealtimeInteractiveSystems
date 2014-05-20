package main.scala.systems.input

import main.scala.tools.{GameConsole, DisplayManager}
import main.scala.math.Mat4f
import main.scala.systems.gfx.Shader
import main.scala.engine.GameEngine
import main.scala.components.Camera

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:17
 *
 * the entire game state
 */
class SimulationContext extends Context {



  var shader: Shader = null
  var modelMatrix: Mat4f = Mat4f.identity
  var viewMatrix: Mat4f = Mat4f.identity
  var displayWidth: Int = 0
  var displayHeight: Int = 0
  var preferredFPS: Int = -1

  private var _aspect: Float = Camera.defaultAspect
  private var _fieldOfView: Float = Camera.defaultFOV
  private var _nearPlane: Float = Camera.defaultNearPlane
  private var _farPlane: Float = Camera.defaultFarPlane


  def aspect:Float = _aspect
  def aspect_=(a:Float) = _aspect = a

  def fieldOfView: Float = _fieldOfView
  def fieldOfView_=(fov:Float) = _fieldOfView = fov

  def nearPlane: Float = _nearPlane
  def nearPlane_=(nP: Float) = _nearPlane = nP

  def farPlane: Float = _farPlane
  def farPlane_=(fP: Float) = _farPlane = fP



  private var _deltaT: Float = 0f


  def setViewMatrix(mat: Mat4f): Mat4f = {
    viewMatrix = mat
    viewMatrix
  }

  def setModelMatrix(mat: Mat4f): Mat4f = {
    modelMatrix = mat
    modelMatrix
  }

  def updateDeltaT(v: Float): Float = {
    _deltaT = v

    deltaT
  }

  def deltaT = _deltaT

  def reset() {  }

  def updateInput(){

    //TODO: https://en.wikipedia.org/wiki/Table_of_keyboard_shortcuts

   Input.keyDownOnceDo(Key._F, _ => DisplayManager.toggleFullscreen()) // FULLSCREEN
   Input.keyDownOnceDo(Key.Esc, _ => GameEngine.shutdown())


       /*
    Input.keyDownDo(Key.ArrowUp,println)
    Input.keyDownDo(Key.ArrowDown,println)
    Input.keyDownDo(Key.ArrowLeft,println)
    Input.keyDownDo(Key.ArrowRight,println)

    Input.keyDownDo(Key.BackSpace,println)


    Input.keyDownDo(Key.ShiftLeft,println)
    Input.keyDownDo(Key.ShiftRight,println)

    Input.keyDownDo(Key.CommandRight,println)
    Input.keyDownDo(Key.CommandLeft,println)

    Input.keyDownDo(Key.AltLeft,println)
    Input.keyDownDo(Key.CtrlLeft,println)

    GameConsole.updateInput()


    if(!GameConsole.isActive) {
    Input.mouseButtonDown(MouseButton.Left, _ => println("FIRE"))


      Key.literals.foreach(l => {
        Input.keyDownOnceDo(l,println)
      })
      Key.numbers.foreach(l => {
        Input.keyDownOnceDo(l,println)
      })

    Input.keyDownOnceDo(Key.Space,_ => println("JUMP"))



    }  */




    //Input.mouseButtonDown(MouseButton.Right,println)
    //Input.mouseButtonDown(MouseButton.Middle,println)


    //println(Keyboard.getEventKey, Keyboard.getKeyName(Keyboard.getEventKey))


  }

  def updateDeltaT(): Long = 0L
}
