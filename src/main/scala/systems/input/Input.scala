package main.scala.systems.input

import main.scala.math.Vec3f
import java.awt.event.{MouseEvent, KeyEvent}
import main.scala.tools.Sys
import javax.swing.SwingUtilities

/**
 * Created by Christian Treffs
 * Date: 20.03.14 13:02
 */
object Input {

  private var input: Input = null
  def init() {

    input = new Input()
  }
  def update() = input.lib.update()

  def mousePosition(): Vec3f = input.lib.getMousePosition
  def mousePositionNormalized(): Vec3f = input.lib.getNormalizedMousePosition
  def mouseButtonDown(mb: Int): Boolean = input.lib.isButtonDown(mb)
  def keyDown(key: Int):Boolean = input.lib.isKeyDown(key)
  def keyToggled(key: Int): Boolean = input.lib.isKeyToggled(key)

}
object MouseButton {
  val Left = MouseEvent.BUTTON1
  val Middle = MouseEvent.BUTTON2
  val Right = MouseEvent.BUTTON3
}
object Key {
  val _A = KeyEvent.VK_A
  val _B = KeyEvent.VK_B
  val _C = KeyEvent.VK_C
  val _D = KeyEvent.VK_D
  val _E = KeyEvent.VK_E
  val _F = KeyEvent.VK_F
  val _G = KeyEvent.VK_G
  val _H = KeyEvent.VK_H
  val _I = KeyEvent.VK_I
  val _J = KeyEvent.VK_J
  val _K = KeyEvent.VK_K
  val _L = KeyEvent.VK_L
  val _M = KeyEvent.VK_M
  val _N = KeyEvent.VK_N
  val _O = KeyEvent.VK_O
  val _P = KeyEvent.VK_P
  val _Q = KeyEvent.VK_Q
  val _R = KeyEvent.VK_R
  val _S = KeyEvent.VK_S
  val _T = KeyEvent.VK_T
  val _U = KeyEvent.VK_U
  val _V = KeyEvent.VK_V
  val _W = KeyEvent.VK_W
  val _X = KeyEvent.VK_X
  val _Y = KeyEvent.VK_Y
  val _Z = KeyEvent.VK_Z

  val _1 = KeyEvent.VK_1
  val _2 = KeyEvent.VK_2
  val _3 = KeyEvent.VK_3
  val _4 = KeyEvent.VK_4
  val _5 = KeyEvent.VK_5
  val _6 = KeyEvent.VK_6
  val _7 = KeyEvent.VK_7
  val _8 = KeyEvent.VK_8
  val _9 = KeyEvent.VK_9
  val _0 = KeyEvent.VK_0

  val Enter = KeyEvent.VK_ENTER
  val BackSpace = KeyEvent.VK_BACK_SPACE
  val Tab = KeyEvent.VK_TAB
  val Command = if(Sys.isMac) KeyEvent.VK_META else if(Sys.isWindows) KeyEvent.VK_WINDOWS else KeyEvent.VK_META
  val Alt = KeyEvent.VK_ALT
  val Ctrl = KeyEvent.VK_CONTROL
  val Space = KeyEvent.VK_SPACE
  val Shift = KeyEvent.VK_SHIFT

  val ArrowUp = KeyEvent.VK_UP
  val ArrowDown = KeyEvent.VK_DOWN
  val ArrowLeft = KeyEvent.VK_LEFT
  val ArrowRight = KeyEvent.VK_RIGHT


}
sealed class Input {

  val lib = new ogl.app.Input()

}
