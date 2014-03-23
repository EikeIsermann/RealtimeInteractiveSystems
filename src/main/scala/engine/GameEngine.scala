package main.scala.engine

import main.scala.architecture.{Family, Engine}
import ogl.app.StopWatch
import main.scala.tools.{GameConsole, DC}
import org.lwjgl.opengl.GL11._
import main.scala.systems.input.{Input, SimulationContext}
import org.lwjgl.opengl.{PixelFormat, GL11, DisplayMode, Display}
import main.scala.io.EntityDescLoader
import main.scala.math.Mat4f
import main.scala.systems.gfx.{RenderingSystem, Shader, Mesh}
import main.scala.entities.Entity
import main.scala.components.Position
import main.scala.nodes.RenderNode

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:34
 */
object GameEngine extends Engine {

  // set debug level
  DC.debugLevel = 0

  private var assetsDir: String = null
  private var gameTitle:String = null
  private var entitiesDir:String = null
  private var shaderDir:String = null
  private var meshesDir:String = null

  private val width = 800
  private val height = 600
  private val prefFPS = 100
  private val FOV = 60f
  private val nearPl = 0.1f
  private val farPl = 100f
  private val multiSampling = false
  private val vSyncEnabled = true
  private var preferredFPS: Int = -1
  private var fieldOfView: Float = -1
  private var nearPlane: Float = -1
  private var farPlane: Float = -1

  //TODO: remove?
  //var entities: SimulationRegistry = null
  var simulationContext: SimulationContext = null
  var time: StopWatch = null



  override def createNewGame(title: String, assetsPath: String = "src/main/resources") = {

    gameTitle = title
    assetsDir = assetsPath

    entitiesDir = assetsDir+"/entities"
    shaderDir = assetsDir+"/shaders"
    meshesDir = assetsDir+"/meshes"

    this

  }

  override def start(): Engine = {
    // init the display
    initDisplay(gameTitle, width, height, FOV, nearPl, farPl, prefFPS,vSyncEnabled, multiSampling)

    // init openGL
    initGL()

    // init game
    initGame()

    //the main game loop
    gameLoop()

    this
  }


  def initDisplay(title: String, width: Int, height: Int, fov: Float, nP: Float, fP: Float, fps: Int, vSync: Boolean = true, multiSampling: Boolean = false) = {
    val dm = new DisplayMode(width, height)

    Display.setDisplayMode(dm)
    Display.setTitle(title)

    if (multiSampling) Display.create(new PixelFormat().withSamples(8))
    else Display.create()


    Display.setVSyncEnabled(vSync)
    preferredFPS = fps
    Display.setSwapInterval(1)
    fieldOfView = fov
    nearPlane = nP
    farPlane = fP

  }

  def initGL() = {
    GL11.glViewport(0, 0, Display.getWidth, Display.getHeight) //TODO: necessary?

    GL11.glMatrixMode(GL11.GL_PROJECTION_MATRIX)
    GL11.glLoadIdentity()

    //GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
    GL11.glOrtho(0, Display.getWidth, Display.getHeight, 0, 1, -1)

    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glLoadIdentity()

    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)  // Black
  }

  def initGame(): Unit = {

    // init a default shader
    val defaultShader = Shader.init()

    // set the default shader as default for all the meshes
    Mesh.defaultShader(defaultShader)

    /*
    // load all stuff
    val colladaFiles = Map[Symbol, String](
      'SkyBox         -> "src/main/resources/meshes/SkyBox/SkyBox.dae",
      'CompanionCube  -> "src/main/resources/meshes/CompanionCube/CompanionCube.dae",
      'Tank           -> "src/main/resources/meshes/T-90/T-90.dae",
      'PhoneBooth     -> "src/main/resources/meshes/PhoneBooth/PhoneBooth.dae",
      'Roads          -> "src/main/resources/meshes/Roads/Roads.dae"
    )

    // load collada files and create meshes -> now everything is available via Mesh.get()
    Collada.load(colladaFiles)
                                                         */
    EntityDescLoader.load(entitiesDir)
    var testEntity = Entity.create("Testwurst")
    var pos = new Position(0,0,100)
    var display = new main.scala.components.Display('ChassisBody, 'wurst)
    testEntity.add(pos)
    testEntity.add(display)
    for(comp <- testEntity.components) DC.log("Test" + comp.toString)
    var fam = new Family(classOf[RenderNode])
    fam.components.+=(classOf[Position], classOf[main.scala.components.Display])
    add(fam)
    for(family <- families.values){
      family.addIfMatch(testEntity)
    }
    add(new RenderingSystem)

    Input.init()

    time = new StopWatch()


    // CREATE SIMULATION CONTEXT
    simulationContext = new SimulationContext()

    // CREATE ENTITY REGISTRY
    // entities = new SimulationRegistry()

    // ADD INITIAL ENTITIES
    //entities += new Cube("Cube1")
    //entities += new MeshEntity(Mesh.get('Turret))
    //entities += new MeshEntity(Mesh.get('ChassisTread))
    //entities += new MeshEntity(Mesh.get('ChassisBody))


    // INIT PHYSICS


    //INITIALIZE ALL ENTITIES
    //entities.initAll(context)

    // set initial deltaT
    simulationContext.updateDeltaT()
  }


  override protected def gameLoop(): Unit = {
    while (!Display.isCloseRequested) {
      //input.update()
      updateContext()
      Display.sync(preferredFPS)

      Input.update(Display.getWidth, Display.getHeight)

      simulate(time.elapsed)

      for(system <- systems.values){system.update(simulationContext)}


      Display.update()
    }

    shutdown()
  }

  override def shutdown(): Unit = {
    DC.log("Shutting down")
    //TODO: stop thread clean up and end
    Display.destroy()
    DC.log("Program Ended")
    System.exit(0)
  }


  /*override def receive: Receive = {
    case ComponentAdded(entity) => componentAdded(entity)
    case ComponentRemoved(entity) => //TODO
  }
*/

  //TODO: to be done in the physics system
  def simulate(elapsed: Float): Unit = {

    // INPUT
    // update user input
    //context.updateInput()
    GameConsole.updateInput(elapsed)


    // PHYSICS
    //simulate all entities
    //entities.simulateAll(context)


  }


  //todo: move variables to context
  def updateContext(): Unit = {
    simulationContext.displayHeight = Display.getHeight
    simulationContext.displayWidth = Display.getWidth
    simulationContext.preferredFPS = preferredFPS
    simulationContext.fieldOfView = fieldOfView
    simulationContext.nearPlane = nearPlane
    simulationContext.farPlane = farPlane

  }



}

