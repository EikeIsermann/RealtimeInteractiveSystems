package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components.{Placement, Camera}
import main.scala.math.Vec3f

/**
 * Created by Eike on
 * 22.03.14.
 */
class CameraNode(camera: Camera, placement:Placement) extends Node(camera, placement) {
  def this() = this(new Camera(90,true),new Placement(Vec3f(0,0,0), Vec3f(0,0,0)))
}
