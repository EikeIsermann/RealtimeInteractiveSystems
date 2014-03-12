package main.scala.tools

import org.lwjgl.opengl.GL11._
/**
 * Created by Christian Treffs
 * Date: 10.03.14 17:14
 */
object HUD {

  def drawString(string: String, color: (Float, Float, Float) = (0,0,0),posX: Float = 0, posY: Float = 0, posZ: Float = 0.5f) {

    glTranslated(posX, posY+10, posZ)
    glRotated(180, 1, 0, 0) //flipped
    glColor3f(color._1, color._2, color._3)
    SimpleText.drawString(string, 0, 0)
    glColor3f(1, 1, 1)

  }

}
