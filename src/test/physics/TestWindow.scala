package test.physics

import javax.swing.JFrame
import java.awt.{Color, Graphics, Dimension}
import java.awt.image.BufferedImage
import main.scala.math.Vec3f
import java.awt.event.{KeyEvent, KeyListener, MouseEvent, MouseListener}
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 05.05.14 15:36
 */
class TestWindow() extends JFrame {

  setTitle("Physics Test")
  val dim = new Dimension(800, 600)
  private var _quit = false

  val bWidth =  790
  val hHeight = 590

  val buffer = new BufferedImage(bWidth, hHeight, BufferedImage.TYPE_INT_ARGB)
  val bufferGraphics = buffer.getGraphics

  var p:Vec3f = Vec3f(0,0,0)
  var rkPOS: Vec3f = Vec3f(0,0,0)

  def quit(): Boolean = _quit

  def mouseP(f: Vec3f) {

    PhyTest.forceToApply = f
  }

  addMouseListener(new MouseListener {
    override def mouseReleased(e: MouseEvent): Unit = {
      mouseP(Vec3f())
    }

    override def mousePressed(e: MouseEvent): Unit = {
      mouseP(Vec3f(900f,-70000f,0))
    }

    override def mouseEntered(e: MouseEvent): Unit = {}

    override def mouseClicked(e: MouseEvent): Unit = {}

    override def mouseExited(e: MouseEvent): Unit = {}
  })

  addKeyListener(new KeyListener {
    override def keyTyped(e: KeyEvent): Unit = {}

    override def keyPressed(e: KeyEvent): Unit = {
      if(e.getKeyCode == KeyEvent.VK_ENTER) {
        _quit = true
      }
    }

    override def keyReleased(e: KeyEvent): Unit = {}
  })

  def update(pos: Vec3f, RKPOS: Vec3f) {


    p = pos
    rkPOS = RKPOS
    repaint()
  }

  val hist = new ArrayBuffer[(Int,Int)]()

  override def paint(g: Graphics) {
    bufferGraphics.clearRect(0,0,bWidth,hHeight)


    bufferGraphics.setColor(Color.RED)
    bufferGraphics.fillRect(math.round(10f+p.x), math.round(400f+p.y), 20, 20)

    bufferGraphics.setColor(Color.BLUE)
    val x: Int = math.round(10f+rkPOS.x)
    val y: Int = math.round(400f+rkPOS.y)
    hist += Tuple2(x,y)
    bufferGraphics.fillRect(x, y, 20, 20)
    hist.foreach(d => {

      bufferGraphics.setColor(Color.WHITE)
      bufferGraphics.drawRect(d._1,d._2,1,1)
    })



    g.drawImage(buffer,0,0,null)

  }

  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setResizable(false)
  setPreferredSize(dim)
  pack()
  repaint()

  setVisible(true)
}
