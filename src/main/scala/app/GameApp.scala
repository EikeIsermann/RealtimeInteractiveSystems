package main.scala.app

import ogl.app.{StopWatch, Input}
import main.scala.tools.DC
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.{PixelFormat, Display, GL11}
import main.scala.world.entities.{SimulationRegistry, Cube}
import main.scala.input.SimulationContext
import java.awt._
import org.lwjgl.LWJGLException

/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:39
 */

object GameApp {
  def main(args: Array[String]) {


    val app = new GameApp
    app.start()


    /*
  val system = ActorSystem("mySystem")

  implicit val myActor = system.actorOf(Props[MyActor], "myActor")

  myActor ! TestMessage("hallo")

  val sig = new Signal[Int](2)

  val func = {x: Int => }

  myActor ! ObserveSignal(sig)(func)

  sig.update(234)

  sig.update(456)     */

  }
}

class GameApp extends Frame with Runnable {



  var entities: SimulationRegistry = null
  var context: SimulationContext = null

  var input: Input = null

  var multisampling = false
  var time: StopWatch = null
  final var canvas: Canvas = null
  final var textField: TextField = null

  var running = false
  var thread: Thread = new Thread(this)






  override def run() {

    try {
      // embed opengl into canvas
      Display.setParent(canvas)

      if (multisampling) Display.create(new PixelFormat().withSamples(8))
      else Display.create()

      Display.setSwapInterval(1)
      Display.setVSyncEnabled(true)
    } catch {
      case e: LWJGLException => e.printStackTrace(); return
    }

    //the game loop
    loop()

  }

  def fullscreen(fullScreen: Boolean = true) {
    if(fullScreen) {
      val screen = GraphicsEnvironment.getLocalGraphicsEnvironment.getDefaultScreenDevice

      setUndecorated(true)
      setResizable(false)
      screen.setFullScreenWindow(this)


    }  else {
      setPreferredSize(new Dimension(800 ,600))
      pack()
      setLocationRelativeTo(null)
    }

  }

  def start() {
    try {

      /*
      val dm = new DisplayMode(width, height)
      Display.setDisplayMode(dm)
      Display.setTitle(title)


      */

      setLayout(new BorderLayout())

      textField = new TextField()
      canvas = new Canvas() {
        override def addNotify() {
          super.addNotify()
          thread.start()
        }
        override def removeNotify() {
          thread.join()
          super.removeNotify()
        }


      }

      //canvas.setSize(getWidth, getHeight)

      add(canvas, BorderLayout.CENTER)
      add(textField, BorderLayout.SOUTH)
      canvas.setFocusable(true)
      textField.setFocusable(true)
      //textField.requestFocus()
      //canvas.requestFocus()
      canvas.setIgnoreRepaint(true)


      fullscreen(fullScreen = false)

      setVisible(true)


      println(KeyboardFocusManager.getCurrentKeyboardFocusManager.getPermanentFocusOwner.toString)

      init()

    }
    catch {
      case e: LWJGLException =>
        e.printStackTrace()
    }

  }

  def init(): Unit = {




    input = new Input
    time = new StopWatch()

    // set debug level
    DC.debugLevel = 0


    // CREATE SIMULATION CONTEXT
    context = new SimulationContext()

    // CREATE ENTITY REGISTRY
    entities = new SimulationRegistry()

    // ADD INITIAL ENTITIES
    entities += new Cube("Cube1")


    // INIT PHYSICS


    //INITIALIZE ALL ENTITIES
    entities.initAll(context)

    // set initial deltaT
    context.updateDeltaT()

  }


  def loop() {
    while (!Display.isCloseRequested) {
      //input.update()
      Display.sync(60)
      //input.setWindowSize(width, height)
      //simulate(time.elapsed, input)
      display(canvas.getWidth, canvas.getHeight)

      Display.update()
    }
  }

  def simulate(elapsed: Float, input: Input): Unit = {

    // INPUT
    // update user input
    context.updateInput(input)


    // PHYSICS
    //simulate all entities
    entities.simulateAll(context)


  }

  def display(width: Int, height: Int): Unit = {
    // Adjust the the viewport to the actual window size. This makes the
    // rendered image fill the entire window.
    glViewport(0, 0, width, height)

    // Clear all buffers.
    glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)



    //shader.activate

    // Assemble the transformation matrix that will be applied to all
    // vertices in the vertex shader.
    //val aspect: Float = width.asInstanceOf[Float] / height.asInstanceOf[Float]

    // The perspective projection. Camera space to NDC.
    //val projectionMatrix: Matrix = vecmath.perspectiveMatrix(60f, aspect, 0.1f, 100f)


    //Shader.setProjectionMatrix(projectionMatrix)

    // display objects

    //camera.activate

    //cube.display

    // GRAPHICS
    // render all entities
//    entities.renderAll(context)


  }
}
