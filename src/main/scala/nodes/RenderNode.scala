package main.scala.nodes

import main.scala.components.{Display, Position}
import main.scala.architecture.{Component, Node}
import scala.collection.mutable
import main.scala.math.Vec3f

/**
 * Render Node
 * links position data (pos, rot, scale) to display data (mesh, shader)
 *
 * Created by Eike
 * 21.03.14.
 */

class RenderNode(position: Position, display: Display) extends Node(position, display){
  def this() = this(new Position(), new Display())
}