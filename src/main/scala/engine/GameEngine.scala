package main.scala.engine

import main.scala.architecture.{Family, Engine}
import ogl.app.StopWatch
import main.scala.tools.{DisplayManager, DC}
import main.scala.systems.input._
import org.lwjgl.opengl.{PixelFormat, GL11, Display}
import main.scala.io.EntityDescLoader
import main.scala.math.Vec3f
import main.scala.systems.gfx.{CameraSystem, RenderingSystem, Shader, Mesh}
import main.scala.entities.Entity
import main.scala.components.{Motion, CamControl, Camera, Position}
import main.scala.nodes.{MovementNode, CamControlNode, CameraNode, RenderNode}
import main.scala.systems.positional.MovementSystem
import main.scala.components.Camera

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:34
 */
object GameEngine extends Engine {

  // set debug level
  DC.debugLevel = 1

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
  private var fps: Float = 0.0f /** frames per second */
  private var lastFPS: Long = -1 /** last fps time */


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
    DC.logT('engineStartup,"Engine", "starting up", 3)
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
    /*val dm = new DisplayMode(width, height)

    Display.setDisplayMode(dm)*/
    DisplayManager.setDisplayMode(width,height,fullscreen = false)
    Display.setResizable(true)

    setGameTitle()

    if (multiSampling) Display.create(new PixelFormat().withSamples(8))
    else Display.create()


    Display.setVSyncEnabled(vSync)
    preferredFPS = fps
    Display.setSwapInterval(1)
    fieldOfView = fov
    nearPlane = nP
    farPlane = fP

    DC.log("Display", "initialized@"+width+"x"+height, 3)
  }

  def initGL() = {


    /*
    GL11.glMatrixMode(GL11.GL_PROJECTION_MATRIX)
    GL11.glLoadIdentity()

    val topClippingPlane    = 0
    val leftClippingPlane   = 0
    val rightClippingPlane  = Display.getWidth
    val bottomClippingPlane = Display.getHeight

    GL11.glOrtho(leftClippingPlane,rightClippingPlane, bottomClippingPlane, topClippingPlane, nearPlane, farPlane)

    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glLoadIdentity()

    GL11.glViewport(0, 0, Display.getWidth, Display.getHeight) //TODO: necessary?*/
    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)  // Black

    DC.log("OpenGL", "initialized", 3)
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

   /* var testEntity = Entity.create("Testwurst")
    var pos = new Position(Vec3f(0,0,2), Vec3f(0,0,0))
    var display = new main.scala.components.Display('Turret, 'wurst)
    testEntity.add(pos)
    testEntity.add(display)
    for(comp <- testEntity.components) DC.log("Test" + comp.toString)
    var fam = new Family(classOf[RenderNode])
    fam.components.+=(classOf[Position], classOf[main.scala.components.Display])
    add(fam)
    for(family <- families.values){
      family.addIfMatch(testEntity)
    }     */

    //SKY BOX TEST
    val skyBoxTestEntity = Entity.create("SkyBoxTest")
    val skyBoxPos = new Position(Vec3f(0,0,0), Vec3f(0,0,0))
    val skyBoxDisplay = new main.scala.components.Display('SkyBox, 'SkyBox)
    skyBoxTestEntity.add(skyBoxPos)
    skyBoxTestEntity.add(skyBoxDisplay)
    for(comp <- skyBoxTestEntity.components) DC.log("SkyBoxTest" + comp.toString)
    val skyBoxFamily = new Family(classOf[RenderNode])
    skyBoxFamily.components.+=(classOf[Position], classOf[main.scala.components.Display])
    add(skyBoxFamily)
    for(family <- families.values){
      family.addIfMatch(skyBoxTestEntity)
    }




    val camEntity = Entity.create("Camera")
    val cam = new Camera(90)
    val camPos = new Position(Vec3f(0,0,0),Vec3f(0,0,0))
    //
    val camCon = new CamControl(Triggers(Key._W),Triggers(Key._S),Triggers(Key._A),Triggers(Key._D),
      Triggers(Key.ArrowUp,null,MouseMovement.MovementY), Triggers(Key.ArrowDown,null,MouseMovement.MovementY),
      Triggers(Key.ArrowLeft,null, MouseMovement.MovementX), Triggers(Key.ArrowRight,null,MouseMovement.MovementX))
    val motion = new Motion()
    camEntity.add(camCon)
    camEntity.add(motion)
    camEntity.add(cam)
    camEntity.add(camPos)


    val camSys = new CameraSystem
    val camFam = new Family(classOf[CameraNode])
    camFam.components += (classOf[Camera], classOf[Position])
    add(camFam)


    val camConSys= new CamControlSystem
    val conFam = new Family(classOf[CamControlNode])
    conFam.components += (classOf[CamControl], classOf[Motion])
    add(conFam)

    val moveSys = new MovementSystem()
    val moveFam = new Family(classOf[MovementNode])
    moveFam.components += (classOf[Position], classOf[Motion])
    add(moveFam)


    families.values.foreach(family => family.addIfMatch(camEntity))
    //families.values.foreach(family => family.addIfMatch(testEntity))
    families.values.foreach(family => family.addIfMatch(skyBoxTestEntity))

    add(camConSys)

    add(moveSys)

    add(camSys)

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

    //FPS
    lastFPS = System.currentTimeMillis()

    // set initial deltaT
    simulationContext.updateDeltaT()

    DC.log("Game","initialized",3)
  }


  override protected def gameLoop(): Unit = {
    DC.logT('engineStartup,"Engine", "initialized", 3)
    while (!Display.isCloseRequested) {
      Display.sync(preferredFPS) //needs to be first

      Input.update() // needs to be before the context update because context depends on fresh key/mouse input?!

      updateContext() // update the context

      systems.values.foreach(_.update(simulationContext)) //update all systems with sim-context


      updateFPS() // update FPS Counter

      Display.update() // show changes
    }

    shutdown()
  }

  override def shutdown(): Unit = {
    DC.logT('engineShutdown,"Engine","shutting down",3)
    //TODO: stop thread clean up and end

    systems.values.foreach(system => system.deinit()) // shut down all systems

    Display.destroy()
    DC.logT('engineShutdown,"Engine","ended",3)
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
    //simulationContext.updateInput()

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
    simulationContext.updateDeltaT(time.elapsed())
   simulationContext.updateInput()
  }


  def setGameTitle(fps: Float = 0, name: String = gameTitle, w: Int = Display.getWidth, h: Int = Display.getHeight) {
    Display.setTitle(name +" @ "+w+"x"+h+" "+ fps +" fps")
  }


  /**
   * Calculate the FPS and set it in the title bar
   *
   * http://www.lwjgl.org/wiki/index.php?title=LWJGL_Basics_4_(Timing)#Calculating_FPS
   */
  def updateFPS() {
    if (System.currentTimeMillis() - lastFPS > 1000) {
      setGameTitle(fps)
      fps = 0 //reset the FPS counter
      lastFPS += 1000 //add one second
    }
    fps = fps +1
  }


}

