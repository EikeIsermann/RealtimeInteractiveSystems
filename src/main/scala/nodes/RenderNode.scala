package main.scala.nodes

import main.scala.components.{Display, Position}
import main.scala.architecture.{Component, Node}
import scala.collection.mutable

/**
 * Created by Eike on 21.03.14.
 */
/*class RenderNode(position: Position, display: Display) extends Node(position, display){

  def this() = {
    this(new Position(0,0,0), new Display('null, 'null))
  }
}*/


class RenderNode() extends Node(){
}