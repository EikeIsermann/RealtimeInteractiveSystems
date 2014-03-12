package main.scala.app

import org.lwjgl.opengl._
import main.scala.io.Collada
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import ogl.app.Util
import main.scala.io.Mesh
import main.scala.math.Mat4f

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



  var mesh: Mesh = null

  var program: Int = -1

  final val vertexAttributeIndex = 0
  final val normalsAttributeIndex = 1
  final val texCoordsAttributeIndex = 2

  var meshes: Seq[Mesh] = Seq.empty[Mesh]

  val vsSource: Array[CharSequence] = Array[CharSequence](
    "uniform mat4 modelMatrix;",
    "uniform mat4 viewMatrix;",
    "uniform mat4 projectionMatrix;",


    "attribute vec3 vertex;",
    "attribute vec3 normal;",
    "attribute vec2 texCoord;",
    "attribute vec3 color;",
    "varying vec3 fcolor;",
    "varying vec2 ftexCoord;",

    "void main() {",
    "  fcolor = color;",
    "  ftexCoord = texCoord;",
    "  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(vertex, 1);",
    "}"
  )

  val fsSource: Array[CharSequence] = Array[CharSequence](
    "varying vec3 fcolor;",
    "varying vec2 ftexCoord;",
    "uniform sampler2D tex;",

    "void main() {",
    " vec4 texColor = texture2D( tex, ftexCoord );",
    "  gl_FragColor = texColor;",
    "}"
  )

  def initGL(width: Int, height: Int) {
    windowWidth = width
    windowHeight = height
    try {
      Display.setDisplayMode(new DisplayMode(width, height))
      Display.create()
      Display.setVSyncEnabled(true)
      Display.sync(100)
    }


    val vs: Int = glCreateShader(GL20.GL_VERTEX_SHADER)
    glShaderSource(vs, vsSource)
    glCompileShader(vs)
    Util.checkCompilation(vs)



    // Create and compile the fragment shader.
    val fs: Int = glCreateShader(GL20.GL_FRAGMENT_SHADER)
    glShaderSource(fs, fsSource)
    glCompileShader(fs)
    Util.checkCompilation(fs)

    program = glCreateProgram
    glAttachShader(program, vs)
    glAttachShader(program, fs)



    glShadeModel  (GL_SMOOTH);			// Enables Smooth Color Shading
    glEnable      (GL_DEPTH_TEST)	// Enables Depth Testing
    glEnable      (GL11.GL_BLEND) // enable alpha blending
    glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)



    GL11.glViewport(0, 0, width, height)

    GL11.glMatrixMode(GL11.GL_PROJECTION_MATRIX)
    GL11.glLoadIdentity()



    //GL11.glOrtho(0, Display.getWidth(), 0, Display.getHeight(), 1, -1);
    GL11.glOrtho(0, Display.getWidth, Display.getHeight, 0, 1, -1)



    GL11.glMatrixMode(GL11.GL_MODELVIEW)
    GL11.glLoadIdentity()



    // Bind the vertex attribute data locations for this shader program. The
    // shader expects to get vertex and color data from the mesh. This needs to
    // be done *before* linking the program.
    glBindAttribLocation(program, vertexAttributeIndex, "vertex")
    glBindAttribLocation(program, normalsAttributeIndex, "normal")
    glBindAttribLocation(program, texCoordsAttributeIndex, "texCoord")


    // Link the shader program.
    glLinkProgram(program)
    Util.checkLinkage(program)


    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
  }


  /**
   * Initialise resources
   */
  def init() {
    val filePaths: Seq[(Symbol,String)] = Seq(
      ('SkyBox, "/Users/ctreffs/Desktop/3DModels/SkyBox/SkyBox.dae"),
     ('CompanionCube, "/Users/ctreffs/Desktop/3DModels/TexturedCube/CompanionCube.dae"),
      ('Tank,"/Users/ctreffs/Desktop/3DModels/T-90/T-90.dae")//,
      //("PhoneBooth","/Users/ctreffs/Desktop/3DModels/Phone_Booth/PhoneBooth.dae"),
      //("Bush","/Users/ctreffs/Desktop/3DModels/Bush/Bush.dae"),
     // ("Roads","/Users/ctreffs/Desktop/3DModels/roads/Roads.dae")//,
      //("SimpleCube","/Users/ctreffs/Desktop/3DModels/SimpleCube/SimpleCube.dae")
    )

    Map[Symbol, String](
      'SkyBox -> ""
    )


    Collada.load(filePaths)

    mesh = Mesh.get('ChassisTread)

  }



  def render() {

    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)

    //TODO: shader, program, position, rot, scale
    mesh.draw(program, vertexAttributeIndex, normalsAttributeIndex, texCoordsAttributeIndex)



    //HUD.drawString("Hello", (1f,1f,1f), 100, 100, -0.5f)


  }

  def main(args: Array[String]) {
    initGL(800, 800)
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
