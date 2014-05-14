package main.scala.nodes

import main.scala.components.{Display, Placement}
import main.scala.architecture.{Component, Node}

/**
 * Render Node
 * links position data (pos, rot, scale) to display data (mesh, shader)
 *
 * Created by Eike
 * 21.03.14.
 */

case class RenderNode(placement: Placement, display: Display) extends Node(placement, display){
  def this() = this(new Placement(), new Display())
}