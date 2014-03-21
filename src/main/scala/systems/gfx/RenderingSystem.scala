package main.scala.systems.gfx


import main.scala.architecture._
import main.scala.systems.input.Context
import main.scala.engine.GameEngine
import main.scala.nodes.RenderNode
import scala.collection.mutable.ListBuffer
import main.scala.components.{Display, Position}
import main.scala.math.Mat4f

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:33
 */
class RenderingSystem extends System {


  override def update(context: Context): System = {
   var nodes = GameEngine.getNodeList(classOf[RenderNode])
   for (node <- nodes){
     var position: Position = node -> (classOf[Position])
     var display: Display = node -> (classOf[Display])
     var mesh = Mesh.getByName(display.meshId)
     //TODO
     var shader = Mesh.defaultShader
     mesh.draw(shader, Mat4f.translation(position.vec), shader.defaultView, shader.defaultProjection)
   }


   this
  }

  override def init(): System = ???
}
