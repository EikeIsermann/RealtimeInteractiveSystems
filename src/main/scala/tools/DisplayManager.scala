package main.scala.tools

import org.lwjgl.opengl._
import org.lwjgl.LWJGLException
import main.scala.systems.input.Input

/**
 * Created by Christian Treffs
 * Date: 22.03.14 21:08
 */
object DisplayManager {



  var currentDisplayMode: DisplayMode = Display.getDisplayMode

  private var fullscreen: Boolean = false

  def toggleFullscreen() {
    if(fullscreen) disableFullscreen()
    else enableFullscreen()

  }

  def enableFullscreen() {
    if(!fullscreen) {
      Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode)
      Input.clear() // ! needs to be here, because Input is in a strange condition on display change
      fullscreen = true
      DC.log("Fullscreen","enabled",1)
    }
  }
  def disableFullscreen() {
    if(fullscreen) {
      Display.setDisplayModeAndFullscreen(currentDisplayMode)
      Input.clear() // ! needs to be here, because Input is in a strange condition on display change
      fullscreen = false
      DC.log("Fullscreen","disabled",1)
    }
  }

  /**
   * Set the display mode to be used
   *
   * @param width The width of the display required
   * @param height The height of the display required
   * @param fullscreen True if we want fullscreen mode
   */
  def setDisplayMode(width: Int, height: Int, fullscreen: Boolean) {

    // return if requested DisplayMode is already set
    if ((Display.getDisplayMode.getWidth == width) && (Display.getDisplayMode.getHeight == height) && (Display.isFullscreen == fullscreen)) {
      return
    }

    try {


      if (fullscreen) {
        val modes: Array[DisplayMode] = Display.getAvailableDisplayModes
        var freq: Int = 0

        for (i <- 0 until modes.length) {
          val current: DisplayMode = modes(i)

          if ((current.getWidth == width) && (current.getHeight == height)) {
            if ((currentDisplayMode == null) || (current.getFrequency >= freq)) {
              if ((currentDisplayMode == null) || (current.getBitsPerPixel > currentDisplayMode.getBitsPerPixel)) {
                currentDisplayMode = current
                freq = currentDisplayMode.getFrequency
              }
            }

            // if we've found a match for bpp and frequence against the
            // original display mode then it's probably best to go for this one
            // since it's most likely compatible with the monitor
            if ((current.getBitsPerPixel == Display.getDesktopDisplayMode.getBitsPerPixel) && (current.getFrequency == Display.getDesktopDisplayMode.getFrequency)) {
              currentDisplayMode = current
              return
            }
          }
        }
      } else {
        currentDisplayMode = new DisplayMode(width, height)
      }

      if (currentDisplayMode == null) {
        System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen)
        return
      }

      Display.setDisplayMode(currentDisplayMode)
      Display.setFullscreen(fullscreen)

    } catch {
      case e: LWJGLException => System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e)
    }
  }

}
