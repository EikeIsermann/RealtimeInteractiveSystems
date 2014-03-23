package main.scala.nodes

import main.scala.components.{Display, Position}
import main.scala.architecture.{Component, Node}
import scala.collection.mutable
import main.scala.math.Vec3f

/**
 * Created by Eike on 21.03.14.
 */
/*class RenderNode(position: Position, display: Display) extends Node(position, display){

  def this() = {
    this(new Position(0,0,0), new Display('null, 'null))
  }
}*/


class RenderNode(position: Position, display: Display) extends Node(position, display){
  def this() = this(new Position(Vec3f(0,0,0), Vec3f(0,0,0)), new Display('null,'null))
}