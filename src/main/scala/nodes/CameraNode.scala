package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Placement, Camera}

/**
 * Created by Eike on
 * 22.03.14.
 */
class CameraNode(camera: Camera, placement:Placement) extends Node(camera, placement) {
  def this() = this(new Camera(),new Placement())
}
