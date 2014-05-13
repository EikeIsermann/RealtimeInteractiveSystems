package main.scala.nodes

import main.scala.components.{Display, Placement}
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

class RenderNode(position: Placement, display: Display) extends Node(position, display){
  def this() = this(new Placement(), new Display())

  var containsNot: List[Class[_ <: Component]] = List()

}