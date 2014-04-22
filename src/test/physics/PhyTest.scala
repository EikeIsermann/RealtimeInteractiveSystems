package test.physics

import main.scala.math.{Mat3f, Vec3f}
import javax.swing.JFrame
import java.awt.{Color, Dimension, Graphics}
import java.awt.image.BufferedImage
import test.physics.millington.{RigidBodyPhysics, ParticlePhysics}
import main.scala.tools.phy
import scala.collection.mutable.ArrayBuffer
import java.awt.event.{MouseEvent, MouseListener}

/**
 * Created by Christian Treffs
 * Date: 03.04.14 18:03
 */
object PhyTest {

  var forceToApply: Vec3f = Vec3f()

  def main(args: Array[String]) {
    val rk = new PhysicsPreImplementation
    val p = new ParticlePhysics()


    val body = new RigidBodyPhysics
    body.mass = 200.0f // 200.0kg
    body.velocity = Vec3f(40.0f, -100.0f, 0) // 50m/s
    body.acceleration = Vec3f(0.0f, 21.0f, 0.0f)
    body.damping_=(0.99f, 0.8f)
    val radius = 0.4f

    body.canSleep = false
    body.awake(awake = true)
    var tensor = new Mat3f()
    val coeff: Float = 0.4f*body.mass*radius*radius
    tensor.setInertiaTensorCoeffs(coeff,coeff,coeff)
    body.inertiaTensor = tensor

    // Set the data common to all particle types
    body.position = Vec3f(0.0f, 1.5f, 0.0f)
    //startTime = TimingData::get().lastFrameTimestamp

    // Clear the force accumulators
    body.calculateDerivedData()
    //calculateInternals();


    val w = new MainWindow()

    p.init(
      Vec3f(0,0,0),
      Vec3f(0,0,0),
      Vec3f(10,-10,0),
      1f, //kg
      0.93f //dampening
    )
    rk.init(
      Vec3f(0,0,0),
      Vec3f(0,0,0),
      Vec3f(3,-9.81f,0),
      1f, //kg
      0.93f //dampening
    )



    var c = 0



    var lastT = phy.timeInSeconds()
    var currentT = 0d
    while (true) {

      //val m = Vec3f(10, 0, 0)

      currentT = phy.timeInSeconds()

      val dt = currentT-lastT
      if(dt > 0.0) {
        p.update(dt)
        val rkPos = rk.update(dt)
        //println(s.position.inline)

        if(forceToApply != Vec3f()) {
          body.addForce(forceToApply)
        }

        if(body.lastFrameAcceleration != Vec3f(0.0f, 21.0f, 0.0f)) {
          println(body.lastFrameAcceleration.inline)
        }

        body.integrate(dt)
        //println(body.position.inline)

        w.update(Vec3f(),body.position)


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
  var rkPOS: Vec3f = Vec3f(0,0,0)

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