package main.scala.engine

import main.scala.architecture.Engine
import ogl.app.StopWatch
import main.scala.tools.{DisplayManager, DC}
import main.scala.systems.input._
import org.lwjgl.opengl.{PixelFormat, GL11, Display}
import main.scala.io.{LevelLoader, EntityTemplateLoader}
import main.scala.systems.gfx._
import main.scala.components._
import main.scala.event._
import main.scala.systems.physics.{PhysicsSystem, CollisionSystem}
import main.scala.systems.sound.SoundSystem
import main.scala.entities.Entity
import main.scala.math.{RISMath, Vec3f}
import main.scala.systems.positional.RelativePositionalSystem
import main.scala.components.CamControl
import main.scala.event.EntityRemoved
import main.scala.systems.input.Triggers
import main.scala.event.EntityCreated
import main.scala.systems.gameplay.{HealthSystem, GunSystem}
import main.scala.components.GunControl
import main.scala.event.EntityRemoved
import main.scala.components.DriveControl
import main.scala.components.Gun
import main.scala.event.EntityCreated
import main.scala.components.Vehicle
import main.scala.systems.ai.aiStates.gunTargetAcquired
import main.scala.systems.ai.GunAISystem

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:34
 */
object GameEngine extends Engine with EventReceiver{

  EventDispatcher.subscribe(classOf[Event])(this)
  // set debug level
  DC.debugLevel = 3

  var showCollisionBox: Boolean = true
  private var assetsDir: String = null
  private var gameTitle:String = null
  private var entitiesDir:String = null
  private var shaderDir:String = null
  private var meshesDir:String = null
  var levelsDir: String = null
  var soundsDir: String = null
  var fontsDir: String = null

  private val width = 1000
  private val height = 600
  private val prefFPS = 60
  private val multiSampling = false
  private val vSyncEnabled = true
  private var preferredFPS: Int = -1
  private var fps: Float = 0.0f /** frames per second */
  private var lastFPS: Long = -1 /** last fps time */

  var ent: Entity = null
  var simulationContext: SimulationContext = null
  var time: StopWatch = null

  override def createNewGame(title: String, assetsPath: String = "src/main/resources") = {

    gameTitle = title
    assetsDir = assetsPath

    entitiesDir = assetsDir+"/entities"
    shaderDir = assetsDir+"/shaders"
    meshesDir = assetsDir+"/meshes"
    levelsDir = assetsDir+"/levels"
    soundsDir = assetsDir+"/sounds"
    fontsDir = assetsDir+"/fonts"

    this

  }

  override def start(): Engine = {
    DC.logT('engineStartup,"Engine", "starting up", 3)
    // init the display
    initDisplay(gameTitle, width, height, prefFPS,vSyncEnabled, multiSampling)

    // init openGL
    initGL()

    // init game
    initGame()

    //the main game loop
    gameLoop()

    this
  }


  def initDisplay(title: String, width: Int, height: Int, fps: Int, vSync: Boolean = true, multiSampling: Boolean = false) = {
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

    DC.log("Display", "initialized@"+width+"x"+height, 3)
  }

  def initGL() = {
    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)  // Black
    DC.log("OpenGL", "initialized", 3)
  }

  def initGame(): Unit = {

    // load shader dir
    Shader.load(shaderDir)

    // init a default shader
    val defaultShader = Shader.init()

    // set the default shader as default for all the meshes
    Mesh.defaultShader(defaultShader)

    // load all entity templates  - needs to happen before level is loaded
    EntityTemplateLoader.load(entitiesDir)

    // loading all level files - or one specific
    //LevelLoader.load()
    // get the level and initialize it
   // LevelLoader.get('testlevel).initialize()


    //create a level from current game with this name and save it to disk
    //val lvl = new Level("TestLevel")
    //lvl.save()
    //creating SkyBox
    Entity.newInstanceOf('SkyBox)

    var tower =     Entity.newInstanceOf('Tower)
    tower.getComponent(classOf[Placement]).position = Vec3f(0,1f,8)


    var tower2 =     Entity.newInstanceOf('Tower)
    tower2.getComponent(classOf[Placement]).position = Vec3f(2,1f,2)

    var tower3 =     Entity.newInstanceOf('Tower)
    tower3.getComponent(classOf[Placement]).position = Vec3f(4,1f,8)

    var tower4 =     Entity.newInstanceOf('Tower)
    tower4.getComponent(classOf[Placement]).position = Vec3f(6,1f,2)

    var tower5 =     Entity.newInstanceOf('Tower)
    tower5.getComponent(classOf[Placement]).position = Vec3f(8,1f,8)

    var tower6 =     Entity.newInstanceOf('Tower)
    tower6.getComponent(classOf[Placement]).position = Vec3f(10,1f,2)

    //creating Floor
    Entity.newInstanceOf('FloorNE).getComponent(classOf[Placement]).position = Vec3f(0,0,0)
    Entity.newInstanceOf('FloorEW).getComponent(classOf[Placement]).position = Vec3f(0,0,2)
    Entity.newInstanceOf('FloorNEWS).getComponent(classOf[Placement]).position = Vec3f(0,0,4)
    Entity.newInstanceOf('FloorNW).getComponent(classOf[Placement]).position = Vec3f(0,0,6)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(0,0,8)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(0,0,10)

    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(2,0,0)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(2,0,2)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(2,0,4)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(2,0,6)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(2,0,8)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(2,0,10)

    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(4,0,0)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(4,0,2)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(4,0,4)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(4,0,6)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(4,0,8)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(4,0,10)

    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(6,0,0)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(6,0,2)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(6,0,4)
    Entity.newInstanceOf('FloorNEWS).getComponent(classOf[Placement]).position = Vec3f(6,0,6)
    Entity.newInstanceOf('FloorEW).getComponent(classOf[Placement]).position = Vec3f(6,0,8)
    Entity.newInstanceOf('FloorNEWS).getComponent(classOf[Placement]).position = Vec3f(6,0,10)

    Entity.newInstanceOf('FloorNEWS).getComponent(classOf[Placement]).position = Vec3f(8,0,0)
    Entity.newInstanceOf('FloorEW).getComponent(classOf[Placement]).position = Vec3f(8,0,2)
    Entity.newInstanceOf('FloorSW).getComponent(classOf[Placement]).position = Vec3f(8,0,4)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(8,0,6)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(8,0,8)
    Entity.newInstanceOf('FloorNS).getComponent(classOf[Placement]).position = Vec3f(8,0,10)

    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(10,0,0)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(10,0,2)
    Entity.newInstanceOf('FloorPLAZA).getComponent(classOf[Placement]).position = Vec3f(10,0,4)
    Entity.newInstanceOf('FloorSE).getComponent(classOf[Placement]).position = Vec3f(10,0,6)
    Entity.newInstanceOf('FloorEW).getComponent(classOf[Placement]).position = Vec3f(10,0,8)
    Entity.newInstanceOf('FloorSW).getComponent(classOf[Placement]).position = Vec3f(10,0,10)


    // creating Tank
    val tank = Entity.newInstanceOf('Tank)

    //val tank2 = Entity.newInstanceOf('Tank)
    val test = entities.apply("Turret:1")
    test.add(new GunControl)
    var test2 = new Gun
    test.add(test2)
    tank.getComponent(classOf[Placement]).position = Vec3f(0,100,10000)
    tank.getComponent(classOf[Placement]).rotation = Vec3f(0,-90,0)

    tank.add(new DriveControl)
    tank.add(new Vehicle)
    ent = tank
    var phys = new Physics()
    phys.canSleep = false
    phys.mass = 60f
    phys.gravity = Vec3f()
    phys.damping_=(0.1f,0.1f)
    phys.gravity_=(Vec3f(0,0,0))
    tank.add(phys)
    phys.canSleep = false
    val cam = new Camera(70f,None,0.1f,50f, true ,Vec3f(0,0,0),Vec3f(-25,0,0), 1.2f )



    test.add(cam)




    var tank1 = Entity.newInstanceOf('Tank)
    tank1.getComponent(classOf[Placement]).position = Vec3f(0,100,0)
    val test3 = entities.apply("Turret:2")
    test3.add(new Gun)
    test3.add(new GunAI(new gunTargetAcquired))

    var tank2 = Entity.newInstanceOf('Tank)
    tank2.getComponent(classOf[Placement]).position = Vec3f(2000,100,8000)


    var tank3 = Entity.newInstanceOf('Tank)
    tank3.getComponent(classOf[Placement]).position = Vec3f(4000,100,2000)


    var tank4 = Entity.newInstanceOf('Tank)
    tank4.getComponent(classOf[Placement]).position = Vec3f(6000,100,0)


    var tank5 = Entity.newInstanceOf('Tank)
    tank5.getComponent(classOf[Placement]).position = Vec3f(6000,100,6000)


    var tank6 = Entity.newInstanceOf('Tank)
    tank6.getComponent(classOf[Placement]).position = Vec3f(10000,100,6000)




    val cam2 = new Camera(70f,None,0.1f,50f, false ,Vec3f(0,0,0),Vec3f(0,0,0),0 )


    val camEntity = Entity.create("Camera")

    val camPos = new Placement(Vec3f(0,1,0),Vec3f(0,0,0))



    val camCon = new CamControl(Triggers(Key._W),Triggers(Key._S),Triggers(Key._A),Triggers(Key._D),
      Triggers(null,null,MouseMovement.MovementY), Triggers(null,null,MouseMovement.MovementY),
      Triggers(null,null, MouseMovement.MovementX), Triggers(null,null,MouseMovement.MovementX), Triggers(Key.Space, null, null), Triggers(Key.CtrlLeft,null,null))



    cam2.active = true
    camEntity.add(camCon)
    //camEntity.add(motion)
    camEntity.add(cam2)
    camEntity.add(camPos)



    Input.init()
    //register systems with engine
    add(new DriveControlSystem)
    add(new CameraSystem)
    add(new CamControlSystem)
    add(new RenderingSystem(prefFPS))
    add(new TextRenderingSystem(prefFPS))
    add(new PhysicsSystem(200))
    add(new CollisionSystem(200))
    add(new GunSystem)
    add(new RelativePositionalSystem)
    add(new SoundSystem)
    add(new GunControlSystem)
    add(new GameControlSystem)
    add(new HealthSystem)
    add(new GunAISystem)


    time = new StopWatch()


    // CREATE SIMULATION CONTEXT
    simulationContext = new SimulationContext()

    //FPS
    lastFPS = System.currentTimeMillis()


    DC.log("Game","initialized",3)


    //val lvl = new Level("TestLevel")
    //lvl.save()
  }




  override protected def gameLoop(): Unit = {
    DC.logT('engineStartup,"Engine", "initialized", 3)
    DC.log("Game","running...",3)
    while (!Display.isCloseRequested) {
      Display.sync(preferredFPS) //needs to be first

      // update the input
      Input.update()

      // update the context
      simulationContext.updateDeltaT(time.elapsed())

      //update all systems with sim-context
      updateSystems(simulationContext)

      updateFPS() // update FPS Counter

      Display.update() // show changes

    }

    shutdown()
  }

  override def shutdown(): Unit = {
    DC.logT('engineShutdown,"Engine","shutting down",3)

    // shut down all systems
    systems.values.foreach(system => system.shutdown())

    Display.destroy()
    DC.logT('engineShutdown,"Engine","ended",3)
    System.exit(0)
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

  override def receive(event: Event): Unit = {
    event match {
      case ec: EntityCreated => add(ec.ent)
      case er: EntityRemoved => remove(er.ent)
      case _ =>

    }
  }
}

