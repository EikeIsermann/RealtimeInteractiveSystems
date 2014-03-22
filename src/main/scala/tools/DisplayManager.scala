package main.scala.tools

import org.lwjgl.opengl._
import org.lwjgl.LWJGLException

/**
 * Created by Christian Treffs
 * Date: 22.03.14 21:08
 */
object DisplayManager {



  var targetDisplayMode: DisplayMode = null

  private var fullscreen: Boolean = false

  def toggleFullscreen() {
    if(fullscreen) disableFullscreen()
    else enableFullscreen()
  }

  def enableFullscreen() {
    if(!fullscreen) {
      Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode)
      fullscreen = true
      DC.log("Fullscreen enabled")
    }
  }
  def disableFullscreen() {
    if(fullscreen) {
      Display.setDisplayModeAndFullscreen(targetDisplayMode)
      fullscreen = false
      DC.log("Fullscreen disabled")
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
      println("one")
      return
    }

    try {


      if (fullscreen) {
        val modes: Array[DisplayMode] = Display.getAvailableDisplayModes
        var freq: Int = 0

        for (i <- 0 until modes.length) {
          val current: DisplayMode = modes(i)

          if ((current.getWidth == width) && (current.getHeight == height)) {
            if ((targetDisplayMode == null) || (current.getFrequency >= freq)) {
              if ((targetDisplayMode == null) || (current.getBitsPerPixel > targetDisplayMode.getBitsPerPixel)) {
                targetDisplayMode = current
                freq = targetDisplayMode.getFrequency
              }
            }

            // if we've found a match for bpp and frequence against the
            // original display mode then it's probably best to go for this one
            // since it's most likely compatible with the monitor
            if ((current.getBitsPerPixel == Display.getDesktopDisplayMode.getBitsPerPixel) && (current.getFrequency == Display.getDesktopDisplayMode.getFrequency)) {
              targetDisplayMode = current
              println("two")
              return
            }
          }
        }
      } else {
        println("three")
        targetDisplayMode = new DisplayMode(width, height)
      }

      if (targetDisplayMode == null) {
        System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen)
        return
      }

      Display.setDisplayMode(targetDisplayMode)
      Display.setFullscreen(fullscreen)

    } catch {
      case e: LWJGLException => System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e)
    }
  }

}
