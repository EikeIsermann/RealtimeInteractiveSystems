package main.scala.systems.gfx


import main.scala.architecture._
import main.scala.nodes.RenderNode
import main.scala.components.Placement
import main.scala.math.Mat4f
import org.lwjgl.opengl.GL11._
import main.scala.components.Display
import org.lwjgl.opengl.GL11

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:33
 */
class RenderingSystem(simSpeed: Int) extends IntervalProcessingSystem {
  override var priority: Int = 0
  override var acc: Float = 0
  override val interval: Float = 1f/simSpeed.toFloat
  override var node: Class[_ <: Node] = classOf[RenderNode]


  def init(): System = this
  override def begin(): Unit = {
    glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)

  }

  override def processNode(node: Node): Unit = {
    node match {
      case renNode: RenderNode =>
        val placementComp: Placement = renNode -> classOf[Placement]
        val displayComp: Display = renNode -> classOf[Display]

        val mesh = Mesh.getByName(displayComp.meshId)
        val shader = Shader.get(displayComp.shaderId)

        val modelMatrix: Mat4f = placementComp.getMatrix

        mesh.draw(shader, modelMatrix, ctx.viewMatrix, ctx.fieldOfView,ctx.aspect,ctx.nearPlane,ctx.farPlane)

      case _ => throw new IllegalArgumentException("not the right node")
    }


  }

  override def end(): Unit = {}

  def deinit(): Unit = {}
}
