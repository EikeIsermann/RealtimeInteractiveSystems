package main.scala.systems.gfx

import main.scala.architecture.{IntervalProcessingSystem, System, Node}
import main.scala.engine.GameEngine
import main.scala.nodes.TextNode
import main.scala.components.{GameConsole, Placement}
import main.scala.math.Mat4f
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL11

/**
 * Created by Christian Treffs
 * Date: 22.05.14 09:15
 */
class TextRenderingSystem(simSpeed: Int) extends IntervalProcessingSystem {

  override var node: Class[_ <: Node] = classOf[TextNode]
  override var priority: Int = 0
  override var acc: Float = 0
  override val interval: Float = 1f/simSpeed.toFloat

  private var font: Font = null

  /**
   * called on system startup
   * @return
   */
  override def init(): System = {
    font = new Font()
    font.init()
    this
  }


  /**
   * executed before nodes are processed - every update
   */
  override def begin(): Unit = {

  }


  override def processNode(node: Node): Unit = {
    node match {
      case tN: TextNode =>

        val placement: Placement = tN -> classOf[Placement]
        val gameConsole: GameConsole = tN -> classOf[GameConsole]


/*        val shader = Shader.get(gameConsole.shaderId)

        val modelMatrix: Mat4f = placement.getMatrix

        font.stringToDraw = gameConsole.stringBuffer.toString()

        font.draw(shader, modelMatrix, ctx.viewMatrix, ctx.fieldOfView,ctx.aspect,ctx.nearPlane,ctx.farPlane)

  */
      case _ =>
    }
  }

  /**
   * executed after nodes are processed - every update
   */
  override def end(): Unit = {}
  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}


}
