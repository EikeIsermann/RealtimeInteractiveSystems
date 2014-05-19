package test.font

import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode


import org.lwjgl.opengl.GL11._
import main.scala.systems.gfx.{Font, Texture}
import main.scala.systems.sound.Audio
import main.scala.systems.input.{Key, Input}

/**
 * Created by Christian Treffs
 * Date: 24.03.14 13:02
 */
object GLMinExample {

  def main(args: Array[String]) {
    initDisplay()
    //glClearColor(0, 0, 0, 1)

    gameLoop()
    shutdown()
  }


  def initDisplay() {

    Display.setDisplayMode(new DisplayMode(800, 800))
    Display.setTitle("GLMin")
    Display.setResizable(true)

    Display.create()


    glClearColor(0, 0, 0, 1)

    Audio.init()


    /*Audio.loadWave('tankFiring, "src/main/resources/sounds/tank-fire.wav")
    Audio.loadWave('tankMoving, "src/main/resources/sounds/tank-moving.wav")

    Audio.createSource('tankFire01, Audio.getBuffer('tankFiring))
    Audio.createSource('tankMove01, Audio.getBuffer('tankMoving))
      */

  }

  def gameLoop() {
    while (!Display.isCloseRequested) {
      //render()
      //input()
      update()
    }
  }

  def render() {
    glClear(GL_COLOR_BUFFER_BIT)
  }

  def  input() {
    while (Keyboard.next()) {
      if (Keyboard.getEventKeyState) {

      }
    }
  }

  def update() {
    Display.update()
    Display.sync(60)

    Input.update()
    /*Input.keyDownOnceDo(Key._1, _ => Audio.getSource('tankFire01).play())
    Input.keyDownOnceDo(Key._2, _ => Audio.getSource('tankMove01).play())
      */


  }


  def shutdown() {
    Audio.deinit()
    Display.destroy()
    System.exit(0)
  }

}
