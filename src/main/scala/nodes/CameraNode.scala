package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Position, Camera}
import main.scala.math.Vec3f

/**
 * Created by Eike on 22.03.14.
 */
class CameraNode(cam: Camera, position:Position) extends Node(cam, position) {
  def this() = this(new Camera(90,true),new Position(Vec3f(0,0,0), Vec3f(0,0,0)))
}
