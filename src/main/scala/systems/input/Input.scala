package main.scala.systems.input

import main.scala.math.Vec3f
import org.lwjgl.input.{Mouse, Keyboard}
import scala.collection.mutable
import main.scala.tools.{DisplayManager, DC}
import org.lwjgl.opengl.Display

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:02
 */
object Input {
  private val pressedKeys = mutable.ArrayBuffer.empty[Int]
  private val toggledKeys = mutable.ArrayBuffer.empty[Int]
  private val pressedMouseButtons = mutable.ArrayBuffer.empty[Int]
  val onKeyPressActions = mutable.HashMap.empty[Int, Unit => Unit]
  val onKeyReleaseActions = mutable.HashMap.empty[Int, Unit => Unit]

  private var _mousePosition: Vec3f = Vec3f(0,0,0)
  private var _mouseMovement: Vec3f = Vec3f(0,0,0)
  val hasWheel = Mouse.hasWheel
  private var _mouseWheel: Int = 0

  def init() {
    DC.log("Input initialized")

    onKeyPressActions.put(Key._1, _ => DisplayManager.enableFullscreen())
    onKeyPressActions.put(Key._2, _ => DisplayManager.disableFullscreen())

  }
  def update() = {
    setMouseMovement()
    iterateMouseButtons()
    iterateKeys()
    setMouseWheel()
  }


  private def iterateKeys() {
    while(Keyboard.next()) {
     val keyId: Int = Keyboard.getEventKey
     Keyboard.getEventKeyState match {
        case true   =>
          //add if pressed
          pressedKeys += keyId

          onKeyPress(keyId)

          // toggle keys
          toggledKeys.contains(keyId) match {
            case true   => toggledKeys -= keyId
            case false  => toggledKeys += keyId
          }
        case false  =>
          pressedKeys -= keyId // remove if released
          onKeyRelease(keyId)
      }
    }
  }

  private def iterateMouseButtons() {
    while(Mouse.next()) {
      val mouseButtonId = Mouse.getEventButton
      if(mouseButtonId != -1) {
        Mouse.getEventButtonState match {
          case true   => pressedMouseButtons += mouseButtonId
          case false  => pressedMouseButtons -= mouseButtonId
        }
      }
    }
  }


  private def setMouseMovement() {
    _mousePosition = Vec3f(Mouse.getX, Mouse.getY, 0.0f)
    _mouseMovement = Vec3f(Mouse.getDX, Mouse.getDY, 0.0f)
  }

  private def setMouseWheel() {
    if(hasWheel) {
      _mouseWheel = Mouse.getDWheel
    }
  }


  def mousePosition(): Vec3f = _mousePosition
  def mousePositionNormalized(windowWidth: Int = Display.getWidth, windowHeight: Int = Display.getHeight): Vec3f = Vec3f(_mousePosition.mapX(0, windowWidth),_mousePosition.mapY(0, windowHeight), 0.0f)
  def mouseMovement(): Vec3f = _mouseMovement
  def mouseWheel: Int = _mouseWheel

  def mouseButtonDown(mb: Int): Boolean = pressedMouseButtons.contains(mb)
  def mouseButtonDown(mb: Int, func: Any => Unit = println) {
    if(mouseButtonDown(mb)) {
      func(mb)
    }
  }

  def keysDown(keys: Int*): Boolean = keys.forall(keyDown)
  def keyDown(key: Int):Boolean = pressedKeys.contains(key)
  def keyDown(key: Int, func: Any => Unit = println) {
    if(keyDown(key)) {
      func(Keyboard.getKeyName(key))
    }
  }
  def keyToggled(key: Int): Boolean = toggledKeys.contains(key)
  def keyToggled(key: Int, func: Any => Unit = println) {
    if(keyToggled(key)) {
      func(Keyboard.getKeyName(key))
    }
  }

  def onKeyPress(keyId: Int) {
    onKeyPressActions.get(keyId).foreach(_.apply())
  }
  def onKeyRelease(keyId: Int) {
    onKeyReleaseActions.get(keyId).foreach(_.apply())
  }

}

object MouseButton {
  val Left = 0
  val Right = 1
  val Middle = 2

}

object Key {
  val _A = Keyboard.KEY_A
  val _B = Keyboard.KEY_B
  val _C = Keyboard.KEY_C
  val _D = Keyboard.KEY_D
  val _E = Keyboard.KEY_E
  val _F = Keyboard.KEY_F
  val _G = Keyboard.KEY_G
  val _H = Keyboard.KEY_H
  val _I = Keyboard.KEY_I
  val _J = Keyboard.KEY_J
  val _K = Keyboard.KEY_K
  val _L = Keyboard.KEY_L
  val _M = Keyboard.KEY_M
  val _N = Keyboard.KEY_N
  val _O = Keyboard.KEY_O
  val _P = Keyboard.KEY_P
  val _Q = Keyboard.KEY_Q
  val _R = Keyboard.KEY_R
  val _S = Keyboard.KEY_S
  val _T = Keyboard.KEY_T
  val _U = Keyboard.KEY_U
  val _V = Keyboard.KEY_V
  val _W = Keyboard.KEY_W
  val _X = Keyboard.KEY_X
  val _Y = Keyboard.KEY_Y
  val _Z = Keyboard.KEY_Z

  val _1 = Keyboard.KEY_1
  val _2 = Keyboard.KEY_2
  val _3 = Keyboard.KEY_3
  val _4 = Keyboard.KEY_4
  val _5 = Keyboard.KEY_5
  val _6 = Keyboard.KEY_6
  val _7 = Keyboard.KEY_7
  val _8 = Keyboard.KEY_8
  val _9 = Keyboard.KEY_9
  val _0 = Keyboard.KEY_0

  val Enter = Keyboard.KEY_RETURN
  val BackSpace = Keyboard.KEY_BACK
  val Tab = Keyboard.KEY_TAB
  val CommandLeft = Keyboard.KEY_LMETA
  val CommandRight = Keyboard.KEY_RMETA
  val AltLeft = Keyboard.KEY_LMENU
  val AltRight = Keyboard.KEY_RMENU
  val CtrlLeft = Keyboard.KEY_LCONTROL
  val CtrlRight = Keyboard.KEY_RCONTROL
  val Space = Keyboard.KEY_SPACE
  val ShiftLeft = Keyboard.KEY_LSHIFT
  val ShiftRight = Keyboard.KEY_RSHIFT

  val GameConsole = Keyboard.KEY_SECTION

  val ArrowUp = Keyboard.KEY_UP
  val ArrowDown = Keyboard.KEY_DOWN
  val ArrowLeft = Keyboard.KEY_LEFT
  val ArrowRight = Keyboard.KEY_RIGHT



}
