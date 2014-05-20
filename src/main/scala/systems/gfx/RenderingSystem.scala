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
class RenderingSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[RenderNode]
  override var priority: Int = 0



  override def begin(): Unit = {
    // Adjust the the viewport to the actual window size. This makes the
    // rendered image fill the entire window.
    glViewport(0, 0, ctx.displayWidth, ctx.displayHeight)


    // Clear all buffers.
    glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)


    //shader.activate


    // Assemble the transformation matrix that will be applied to all
    // vertices in the vertex shader.
    val aspect: Float = ctx.displayWidth.asInstanceOf[Float] / ctx.displayHeight.asInstanceOf[Float]





    //context.setViewMatrix(Mat4f.translation(0,0,0))
    val mat = Mat4f.translation(0, 0, -0.8f).mult(Mat4f.rotation(0, 1, 0, 90f)).mult(Mat4f.scale(0.001f, 0.001f, 0.001f))
    ctx.setModelMatrix(mat)



    // The perspective projection. Camera space to NDC.

    //Shader.setProjectionMatrix(projectionMatrix)

    // display objects

    //camera.activate

    //cube.display

    // GRAPHICS
    // render all entities
    // entities.renderAll(context)
  }

  override def processNode(node: Node): Unit = {
    node match {
      case renNode: RenderNode => {
        val placementComp: Placement = renNode -> classOf[Placement]
        val displayComp: Display = renNode -> classOf[Display]

        val meshId = displayComp.meshId
        val shaderId = displayComp.shaderId
        val position = placementComp.position
        val rotation = placementComp.rotation
        val scale = placementComp.scale


        //println("PE: "+placementComp.owner,position.inline,rotation.inline,scale.inline)
        //println("\t"+GameEngine.entities.values.toList)


        val mesh = Mesh.getByName(meshId)


        val shader = Shader.get(shaderId)


        //TODO use parent and child construct to get the right trafos!
        val modelMatrix: Mat4f = placementComp.getMatrix //Mat4f.rotation(rotation) * Mat4f.translation(position) * Mat4f.scale(scale)


        mesh.draw(shader, modelMatrix, ctx.viewMatrix, ctx.fieldOfView,ctx.aspect,ctx.nearPlane,ctx.farPlane)
      }
      case _ => throw new IllegalArgumentException("not the right node")
    }


  }

  override def end(): Unit = {}


  def init(): System = {
    this
  }

  def deinit(): Unit = {}

  /*def update(context: SimulationContext): System = {



    val nodes = GameEngine.getNodeList(new RenderNode())
    for (node <- nodes) {
      val positionNode: Placement = node -> classOf[Placement]
      val displayNode: Display = node -> classOf[Display]


      val meshId = displayNode.meshId
      val shaderId = displayNode.shaderId
      val position = positionNode.position
      val rotation = positionNode.rotation
      val scale = positionNode.scale

      val mesh = Mesh.getByName(meshId)

      val shader = Shader.get(shaderId)


      //TODO use parent and child construct to get the right trafos!
      val modelMatrix: Mat4f = Mat4f.rotation(position) * Mat4f.translation(rotation) * Mat4f.scale(scale)



      mesh.draw(shader, modelMatrix, context.projectionMatrix, context.viewMatrix)
    }


    this
  }     */




}
