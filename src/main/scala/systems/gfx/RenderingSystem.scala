package main.scala.systems.gfx


import main.scala.architecture._
import main.scala.systems.input.{SimulationContext, Context}
import main.scala.engine.GameEngine
import main.scala.nodes.RenderNode
import main.scala.components.{Display, Position}
import main.scala.math.Mat4f
import main.scala.tools.DC
import org.lwjgl.opengl.GL11._
import main.scala.components.Display
import org.lwjgl.opengl.GL11

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:33
 */
class RenderingSystem extends System {


   def update(context: SimulationContext): System = {

     // Adjust the the viewport to the actual window size. This makes the
     // rendered image fill the entire window.
     glViewport(0, 0, context.displayWidth, context.displayHeight)


     // Clear all buffers.
     glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)


     //shader.activate


     // Assemble the transformation matrix that will be applied to all
     // vertices in the vertex shader.
     val aspect: Float = context.displayWidth.asInstanceOf[Float] / context.displayHeight.asInstanceOf[Float]


     context.setProjectionMatrix(Mat4f.projection(context.fieldOfView, aspect, context.nearPlane, context.farPlane))
     //context.setViewMatrix(Mat4f.translation(0,0,0))
     val mat = Mat4f.translation(0,0,-0.8f).mult(Mat4f.rotation(0,1,0, 90f)).mult(Mat4f.scale(0.001f, 0.001f, 0.001f))
     context.setModelMatrix(mat)


     // The perspective projection. Camera space to NDC.

     //Shader.setProjectionMatrix(projectionMatrix)

     // display objects

     //camera.activate

     //cube.display

     // GRAPHICS
     // render all entities
     // entities.renderAll(context)

     val nodes = GameEngine.getNodeList(classOf[RenderNode])
     for (node <- nodes){
         val position: Position = node -> classOf[Position]
         val display: Display = node -> classOf[Display]
         val mesh = Mesh.getByName(display.meshId)
         //TODO
         val shader = Mesh.defaultShader
         mesh.draw(shader, Mat4f.translation(position.position), context.viewMatrix, context.projectionMatrix)
       }


   this
  }

  override def init(): System = {
    DC.log("Rendering System","initialized",2)
    this
  }

  override def deinit(): Unit = {
    DC.log("Rendering System","ended",2)
  }
}
