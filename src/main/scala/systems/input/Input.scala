package main.scala.systems.input

import main.scala.math.Vec3f
import org.lwjgl.input.{Mouse, Keyboard}
import scala.collection.mutable
import main.scala.tools.DC
import org.lwjgl.opengl.Display
import scala.reflect.ClassTag

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:02
 */
object Input {
  //TODO: https://en.wikipedia.org/wiki/Table_of_keyboard_shortcuts

  private val pressedKeys = mutable.ArrayBuffer.empty[Int]
  private val pressedOnceKeys = mutable.ArrayBuffer.empty[Int]
  private val toggledKeys = mutable.ArrayBuffer.empty[Int]
  private val pressedMouseButtons = mutable.ArrayBuffer.empty[Int]

  private var _mousePosition: Vec3f = Vec3f(0,0,0)
  private var _mouseMovement: Vec3f = Vec3f(0,0,0)
  val hasWheel = Mouse.hasWheel
  private var _mouseWheel: Int = 0

  def init() {
    Mouse.setGrabbed(true) // grab the mouse
    DC.log("Input","initialized")

  }
  def update() = {
    setMouseMovement()
    iterateMouseButtons()
    iterateKeys()
    setMouseWheel()
  }

  def clear() {
    pressedKeys.clear()
    pressedOnceKeys.clear()
    toggledKeys.clear()
    pressedMouseButtons.clear()
    DC.log("Input","cleared")
  }


  private def iterateKeys() {
    while(Keyboard.next()) {

     val keyId: Int = Keyboard.getEventKey

     Keyboard.getEventKeyState match {
        case true   =>
          //add if pressed
          pressedKeys += keyId

          // toggle keys
          toggledKeys.contains(keyId) match {
            case true   => toggledKeys -= keyId
            case false  => toggledKeys += keyId
          }
        case false  =>
          pressedKeys -= keyId // remove if released
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

  def mouseButtonDown(mb: MouseButton.Value): Boolean = mb != null && pressedMouseButtons.contains(mb.id)
  def mouseButtonDownDo(mb: MouseButton.Value, func: Any => Unit = println) {
    if(mouseButtonDown(mb)) {
      func(mb)
    }
  }



  def mousePositionNormalizedDo(mv: MouseMovement.Value, func: (Vec3f,Vec3f,Vec3f) => Unit) {
    func(mousePosition(),mousePositionNormalized(),mouseMovement())
  }
  def mouseMovementDo(mv: MouseMovement.Value, func: Vec3f => Unit) {
    func(mouseMovement())
  }

  def isKeyDown(key: Key.Value):Boolean = key != null && pressedKeys.contains(key.id)
  def isKeyDownOnce(key: Key.Value): Boolean = {
    if(isKeyDown(key) && !pressedOnceKeys.contains(key.id)) {
      pressedOnceKeys += key.id
      true
    }
    else if (isKeyDown(key) && pressedOnceKeys.contains(key.id)) {
      false
    } else {
      pressedOnceKeys -= key.id
      false
    }
  }
  def isKeyToggled(key: Key.Value): Boolean = key != null && toggledKeys.contains(key.id)



  def keyDownDo(key: Key.Value, func: Any => Unit = println) {
    if(isKeyDown(key)) {
      func(key)
    }
  }
  def keyDownOnceDo(key: Key.Value, func: Any => Unit) {
    if(isKeyDownOnce(key)) {
      func(key)
    }
  }
  def keyToggledDo(key: Key.Value, func: Any => Unit = println) {
    if(isKeyToggled(key)) {
      func(key)
    }
  }

  def areKeysDown(keys: Key.Value*): Boolean = keys.forall(isKeyDown)

  //def allControls: Seq[Trigger#Value] = Key.values.toSeq ++ MouseButton.values.toSeq ++ MouseMovement.values.toSeq



}


case class Triggers(key1: Key.Value = null, mouseButton1: MouseButton.Value = null, mouseMovement1: MouseMovement.Value = null) {


  private var _key: Key.Value = key1
  private var _mouseButton: MouseButton.Value = mouseButton1
  private var _mouseMovement: MouseMovement.Value = mouseMovement1

  def key = _key
  def key_=(k: Key.Value) = _key = k

  def mouseButton = _mouseButton
  def mouseButton_=(mb: MouseButton.Value) = _mouseButton = mb

  def mouseMovement = _mouseMovement
  def mouseMovement_=(mv: MouseMovement.Value) = _mouseMovement = mv
}

trait Trigger extends Enumeration {
  def classTag: ClassTag[_]
}


object MouseMovement extends Trigger {
  val MovementX,MovementY,Wheel = Value

  override def classTag = scala.reflect.classTag[MouseMovement.Value]


}


object MouseButton extends Trigger {

  val Left = Value(0)
  val Right = Value(1)
  val Middle = Value(2)


  override def classTag = scala.reflect.classTag[MouseButton.Value]
}


object Key extends Trigger  {


  override def classTag = scala.reflect.classTag[Key.Value]


  val Function = Value(Keyboard.KEY_FUNCTION)

  val Enter = Value(Keyboard.KEY_RETURN)
  val BackSpace = Value(Keyboard.KEY_BACK)
  val Tab = Value(Keyboard.KEY_TAB)
  val Space = Value(Keyboard.KEY_SPACE)
  val GameConsole = Value(Keyboard.KEY_SECTION)


  val Esc = Value(Keyboard.KEY_ESCAPE)


    val F1 = Value(Keyboard.KEY_F1)
    val F2 = Value(Keyboard.KEY_F2)
    val F3 = Value(Keyboard.KEY_F3)
    val F4 = Value(Keyboard.KEY_F4)
    val F5 = Value(Keyboard.KEY_F5)
    val F6 = Value(Keyboard.KEY_F6)
    val F7 = Value(Keyboard.KEY_F7)
    val F8 = Value(Keyboard.KEY_F8)
    val F9 = Value(Keyboard.KEY_F9)
    val F10 = Value(Keyboard.KEY_F10)
    val F11 = Value(Keyboard.KEY_F11)
    val F12 = Value(Keyboard.KEY_F12)



    val _1 = Value(Keyboard.KEY_1)
    val _2 = Value(Keyboard.KEY_2)
    val _3 = Value(Keyboard.KEY_3)
    val _4 = Value(Keyboard.KEY_4)
    val _5 = Value(Keyboard.KEY_5)
    val _6 = Value(Keyboard.KEY_6)
    val _7 = Value(Keyboard.KEY_7)
    val _8 = Value(Keyboard.KEY_8)
    val _9 = Value(Keyboard.KEY_9)
    val _0 = Value(Keyboard.KEY_0)



    val ArrowUp = Value(Keyboard.KEY_UP)
    val ArrowDown = Value(Keyboard.KEY_DOWN)
    val ArrowLeft = Value(Keyboard.KEY_LEFT)
    val ArrowRight = Value(Keyboard.KEY_RIGHT)



    val CtrlLeft = Value(Keyboard.KEY_LCONTROL)
    val CtrlRight = Value(Keyboard.KEY_RCONTROL)



    val CommandLeft = Value(Keyboard.KEY_LMETA)
    val CommandRight = Value(Keyboard.KEY_RMETA)



    val AltLeft = Value(Keyboard.KEY_LMENU)
    val AltRight = Value(Keyboard.KEY_RMENU)



    val ShiftLeft = Value(Keyboard.KEY_LSHIFT)
    val ShiftRight = Value(Keyboard.KEY_RSHIFT)


    val Comma = Value(Keyboard.KEY_COMMA)
    val Period = Value(Keyboard.KEY_PERIOD)

    val _A = Value(Keyboard.KEY_A)
    val _B = Value(Keyboard.KEY_B)
    val _C = Value(Keyboard.KEY_C)
    val _D = Value(Keyboard.KEY_D)
    val _E = Value(Keyboard.KEY_E)
    val _F = Value(Keyboard.KEY_F)
    val _G = Value(Keyboard.KEY_G)
    val _H = Value(Keyboard.KEY_H)
    val _I = Value(Keyboard.KEY_I)
    val _J = Value(Keyboard.KEY_J)
    val _K = Value(Keyboard.KEY_K)
    val _L = Value(Keyboard.KEY_L)
    val _M = Value(Keyboard.KEY_M)
    val _N = Value(Keyboard.KEY_N)
    val _O = Value(Keyboard.KEY_O)
    val _P = Value(Keyboard.KEY_P)
    val _Q = Value(Keyboard.KEY_Q)
    val _R = Value(Keyboard.KEY_R)
    val _S = Value(Keyboard.KEY_S)
    val _T = Value(Keyboard.KEY_T)
    val _U = Value(Keyboard.KEY_U)
    val _V = Value(Keyboard.KEY_V)
    val _W = Value(Keyboard.KEY_W)
    val _X = Value(Keyboard.KEY_X)
    val _Y = Value(Keyboard.KEY_Y)
    val _Z = Value(Keyboard.KEY_Z)


  def literals: Seq[Key.Value] = Seq(_A,_B,_C,_D,_E,_F,_G,_H,_I,_J,_K,_L,_M,_N,_O,_P,_Q,_R,_S,_T,_U,_V,_W,_X,_Y,_Z)
  def numbers: Seq[Key.Value] = Seq(_0,_1,_2,_3,_4,_5,_6,_7,_8,_9)

  def asChar(key: Key.Value): Char = {
    key match {
      case Key._A => 'a'
      case Key._B => 'b'
      case Key._C => 'c'
      case Key._D => 'd'
      case Key._E => 'e'
      case Key._F => 'f'
      case Key._G => 'g'
      case Key._H => 'h'
      case Key._I => 'i'
      case Key._J => 'j'
      case Key._K => 'k'
      case Key._L => 'l'
      case Key._M => 'm'
      case Key._N => 'n'
      case Key._O => 'o'
      case Key._P => 'p'
      case Key._Q => 'q'
      case Key._R => 'r'
      case Key._S => 's'
      case Key._T => 't'
      case Key._U => 'u'
      case Key._V => 'v'
      case Key._W => 'w'
      case Key._X => 'x'
      case Key._Y => 'y'
      case Key._Z => 'z'
      case Key._0 => '0'
      case Key._1 => '1'
      case Key._2 => '2'
      case Key._3 => '3'
      case Key._4 => '4'
      case Key._5 => '5'
      case Key._6 => '6'
      case Key._7 => '7'
      case Key._8 => '8'
      case Key._9 => '9'
      case Key.Space => ' '
      case Key.Period => '.'
      case Key.Comma => ','
      case _ => throw new IllegalArgumentException("can't compute character "+key)

    }
  }


}





