package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components.{Placement, Camera, Motion, CamControl}
import main.scala.systems.input._

/**
 * Created by Christian Treffs
 * Date: 23.03.14 09:34
 */
class CamControlNode(camCon: CamControl, cam: Camera, pos: Placement) extends Node {


  def this() = this(
    new CamControl(Triggers(),Triggers(),Triggers(),Triggers(),Triggers(), Triggers(), Triggers(), Triggers(), Triggers(), Triggers()),
    new Camera(90), new Placement()
  )
}
