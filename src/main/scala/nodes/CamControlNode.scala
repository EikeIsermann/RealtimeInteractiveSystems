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

  def this() = this(new CamControl(Seq(Key._A),Seq(Key._W),Seq(Key._D),Seq(Key._S),Seq(), Seq(), Seq(), Seq()), new Motion(Vec3f(0,0,0), 1))

}
