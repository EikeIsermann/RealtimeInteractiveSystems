package main.scala.app

import org.lwjgl.opengl._
import main.scala.io.Collada
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import ogl.app.Util
import main.scala.io.Mesh
import main.scala.math.{Vec3f, Mat4f}
import main.scala.shader.Shader
import main.scala.tools.HUD

/**
 * Created by Christian Treffs
 * Date: 10.03.14 17:33
 */
object TestApp {


  var windowHeight =0
  var windowWidth = 0
  var xPos = 10
  var yPos = 10

  var offsetX = 0
  var offsetY = 0

  var i: Int = 0
  var l: Int = -1



  var cube: Mesh = null
  var tankChassisTread: Mesh = null
  var tankChassisBody: Mesh = null

  var program: Int = -1

  final val vertexAttributeIndex = 0
  final val normalsAttributeIndex = 1
  final val texCoordsAttributeIndex = 2

  var meshes: Seq[Mesh] = Seq.empty[Mesh]



  def initGL(width: Int, height: Int) {
    windowWidth = width
    windowHeight = height
    try {
      Display.setDisplayMode(new DisplayMode(width, height))
      Display.create()
      Display.setVSyncEnabled(true)
      Display.sync(100)
    }

    GL11.glViewport(0, 0, width, height)

    GL11.glMatrixMode(GL11.GL_PROJECTION_MATRIX)
    GL11.glLoadIdentity()

    //GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
    GL11.glOrtho(0, Display.getWidth, Display.getHeight, 0, 1, -1)

    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glLoadIdentity()


    // init the shader
    val defaultShader = Shader.init()



    // set the shader as default for all the meshes
    Mesh.defaultShader(defaultShader)



    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
  }


  /**
   * Initialise resources
   */
  def init() {


    val colladaFiles = Map[Symbol, String](
      'SkyBox         -> "src/main/resources/SkyBox/SkyBox.dae",
      'CompanionCube  -> "src/main/resources/CompanionCube/CompanionCube.dae",
      'Tank           -> "src/main/resources/T-90/T-90.dae",
      'PhoneBooth     -> "src/main/resources/PhoneBooth/PhoneBooth.dae",
      'Roads          -> "src/main/resources/Roads/Roads.dae"
    )


    Collada.load(colladaFiles)



    //mesh = Mesh.get('CompanionCube)

    Mesh.get('CompanionCube).init(Vec3f(-0.2f,0.2f,-0.5f), Vec3f(0.04f, 0.04f, 0.04f), (Vec3f(1, 1,0), 45f))


    val rot = (Vec3f(0,1,1), 90f)
    val scale = Vec3f(0.0004f, 0.0004f, 0.0004f)
    val pos = Vec3f(0,0f,-0.5f)



    Mesh.get('ChassisBody).init(pos, scale, rot)
    Mesh.get('ChassisTread).init(pos, scale, rot)

    Mesh.get('Turret).init(pos, scale, rot)


  }




  def render() {

    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)

    /*Mesh.get('CompanionCube).draw()

    Mesh.get('ChassisBody).draw()
    Mesh.get('ChassisTread).draw()



    Mesh.get('Turret).draw()
    Mesh.get('MachineGun).draw()    */

    //HUD.drawString("Hello", (1f,1f,1f), 0, 0, -0.5f)


  }

  def main(args: Array[String]) {
    initGL(900, 900)
    init()

    while (true) {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT)
      render()

      Display.update()
      Display.sync(100)

      if (Display.isCloseRequested) {
        Display.destroy()
        System.exit(0)
      }
    }


  }

}
