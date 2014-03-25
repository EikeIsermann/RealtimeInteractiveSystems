package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Motion, CamControl}
import main.scala.math.Vec3f
import main.scala.systems.input.Key

/**
 * Created by Christian Treffs
 * Date: 23.03.14 09:34
 */
class CamControlNode(camCon: CamControl, mot: Motion) extends Node {

  def this() = this(new CamControl(Key._A,Key._W,Key._D,Key._S), new Motion(Vec3f(0,0,0), 1))

}
