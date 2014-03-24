package test.font

import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.Display
import org.lwjgl.opengl.DisplayMode


import org.lwjgl.opengl.GL11._
import main.scala.systems.gfx.{Font, Texture}

/**
 * Created by Christian Treffs
 * Date: 24.03.14 13:02
 */
object FontRendering {

  private var fontTextureIdx: Int = -1
  private val textureFilePath: String = "src/main/resources/fonts/Arial@54@1024x@alpha.png"

  private final val renderString: StringBuilder = new StringBuilder("Enter your text")

  def main(args: Array[String]) {
    initDisplay()
    initTextures()

    glClearColor(0, 0, 0, 1)

    gameLoop()

    shutdown()
  }


  def initDisplay() {
    Display.setDisplayMode(new DisplayMode(800, 800))
    Display.setTitle("Font Test")
    Display.setResizable(true)
    Display.create()
  }

  def initTextures() {
    //fontTextureIdx = Texture.load(textureFilePath, flipped = false).texId


    Font.init()

  }

  def gameLoop() {
    while (!Display.isCloseRequested) {
      render()
      input()
      update()
    }
  }

  def render() {
    glClear(GL_COLOR_BUFFER_BIT)
    renderString(renderString.toString(), fontTextureIdx, 16,  -0.9f, 0, 0.3f, 0.225f)
  }

  def  input() {
    while (Keyboard.next()) {
      if (Keyboard.getEventKeyState) {
        // Reset the string if we press escape.
        if (Keyboard.getEventKey == Keyboard.KEY_ESCAPE) {
          renderString.setLength(0)
        }
        // Append the pressed key to the string if the key isn't the back key or the shift key.
        if (Keyboard.getEventKey != Keyboard.KEY_BACK) {
          if (Keyboard.getEventKey != Keyboard.KEY_LSHIFT) {
            renderString.append(Keyboard.getEventCharacter)
            //                        renderString.append((char) Keyboard.getEventCharacter() - 1)
          }
          // If the key is the back key, shorten the string by one character.
        } else if (renderString.length > 0) {
          renderString.setLength(renderString.length - 1)
        }
      }
    }
  }

  def update() {
    Display.update()
    Display.sync(60)
  }


  def shutdown() {
    glDeleteTextures(fontTextureIdx)
    Display.destroy()
    System.exit(0)
  }

  def renderString( string: String, textureObject: Int,  gridSize: Int,  x: Float,  y: Float, characterWidth: Float, characterHeight: Float) {


    //Texture.bind(textureObject)

    // Store the current model-view matrix.
    /*glPushMatrix()
    // Offset all subsequent (at least up until 'glPopMatrix') vertex coordinates.
    glTranslatef(0, 0, 0)
      */

    Font.render(string)


    /*
    glPopMatrix()
    glPopAttrib()
    */
  }
}
