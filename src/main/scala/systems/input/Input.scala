package main.scala.systems.input

import main.scala.math.Vec3f
import org.lwjgl.input.Keyboard

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:02
 */
object Input {

  private var input: Input = null
  //private val actionMap = mutable.HashMap.empty[Symbol, AnyRef => AnyRef]
  def init() {

    input = new Input()
  }
  def update() = input.lib.update()


  //def addAction(name: Symbol, function: AnyRef => AnyRef)= actionMap.put(name,function)
  //def getAction(name:Symbol) = actionMap(name)

  def mousePosition(): Vec3f = input.lib.getMousePosition
  def mousePositionNormalized(): Vec3f = input.lib.getNormalizedMousePosition
  def mouseButtonDown(mb: Int): Boolean = input.lib.isButtonDown(mb)
  def mouseButtonDown(mb: Int, func: Any => Unit = println) {
    if(mouseButtonDown(mb)) {
      func(mb)
    }
  }

  def keysDown(keys: Int*): Boolean = keys.forall(keyDown)
  def keyDown(key: Int):Boolean = input.lib.isKeyDown(key)
  def keyDown(key: Int, func: Any => Unit = println) {
    if(keyDown(key)) {
      func(Keyboard.getKeyName(key))
    }
  }
  def keyToggled(key: Int): Boolean = input.lib.isKeyToggled(key)
  def keyToggled(key: Int, func: Any => Unit = println) {
    if(keyToggled(key)) {
      func(Keyboard.getKeyName(key))
    }
  }

  def windowSize(dims: (Int, Int)) = input.lib.setWindowSize(dims._1, dims._2)

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

  val ArrowUp = Keyboard.KEY_UP
  val ArrowDown = Keyboard.KEY_DOWN
  val ArrowLeft = Keyboard.KEY_LEFT
  val ArrowRight = Keyboard.KEY_RIGHT



}
sealed class Input {

  val lib = new ogl.app.Input()

}
