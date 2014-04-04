package test.physics

import main.scala.math.Vec3f
import javax.swing.JFrame
import java.awt.{Dimension, Graphics}
import java.awt.image.BufferedImage
import test.physics.millington.ParticlePhysics
import main.scala.tools.phy

/**
 * Created by Christian Treffs
 * Date: 03.04.14 18:03
 */
object PhyTest {


  def main(args: Array[String]) {
    val p = new PhysicsPreImplementation
    //val p = new ParticlePhysics()


    val w = new MainWindow()

    p.init(
      Vec3f(0,0,0),
      Vec3f(350,-800,0),
      Vec3f(0,900.0f,0)
    )



    var c = 0


    var lastT = phy.timeInSeconds()
    var currentT = 0d
    while (true) {

      //val m = Vec3f(10, 0, 0)

      currentT = phy.timeInSeconds()

      val dt = currentT-lastT
      if(dt > 0.0) {
        val s = p.update(dt)
        //println(s.position.inline)
        w.update(s.position)
        //w.update(p.position)
      }
      lastT = currentT
      c += 1
    }

    p.deinit()
  }
}

class MainWindow() extends JFrame {

  setTitle("Physics Test")
  val dim = new Dimension(800, 600)

  val bWidth =  700
  val hHeight = 500

  val buffer = new BufferedImage(bWidth, hHeight, BufferedImage.TYPE_INT_ARGB)
  val bufferGraphics = buffer.getGraphics

  var p:Vec3f = Vec3f(0,0,0)

  def update(pos: Vec3f) {

    p = pos
    repaint()
  }

  override def paint(g: Graphics) {
    bufferGraphics.clearRect(0,0,bWidth,hHeight)

    bufferGraphics.fillRect(math.round(10f+p.x), math.round(400f+p.y), 20, 20)



    g.drawImage(buffer,0,0,null)

  }

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setResizable(false)
  setPreferredSize(dim)
  pack()
  repaint()

  setVisible(true)
}