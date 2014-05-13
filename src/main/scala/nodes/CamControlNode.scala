package main.scala.nodes

import main.scala.architecture.{Component, Node}
import main.scala.components.{Motion, CamControl}
import main.scala.systems.input._

/**
 * Created by Christian Treffs
 * Date: 23.03.14 09:34
 */
class CamControlNode(camCon: CamControl, mot: Motion) extends Node {


  def this() = this(
    new CamControl(Triggers(),Triggers(),Triggers(),Triggers(),Triggers(), Triggers(), Triggers(), Triggers(), Triggers(), Triggers()),
    new Motion()
  )

  var containsNot: List[Class[_ <: Component]] = List()
}
