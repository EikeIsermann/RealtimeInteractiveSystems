package main.scala.systems.gfx


import main.scala.architecture._
import main.scala.systems.input.Context
import main.scala.engine.GameEngine
import main.scala.nodes.RenderNode
import main.scala.components.{Display, Position}
import main.scala.math.Mat4f
import main.scala.tools.DC

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:33
 */
class RenderingSystem extends System {


  override def update(context: Context): System = {
    val nodes = GameEngine.getNodeList(classOf[RenderNode])
   for (node <- nodes){
     val position: Position = node -> classOf[Position]
     val display: Display = node -> classOf[Display]
     val mesh = Mesh.getByName(display.meshId)
     //TODO
     val shader = Mesh.defaultShader
     mesh.draw(shader, Mat4f.translation(position.vec), shader.defaultView, shader.defaultProjection)
   }


   this
  }

  override def init(): System = {
    DC.log("Rendering System initialized")
    this
  }
}
