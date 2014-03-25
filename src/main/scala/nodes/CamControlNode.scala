package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Motion, CamControl}
import main.scala.math.Vec3f
import main.scala.systems.input._

/**
 * Created by Christian Treffs
 * Date: 23.03.14 09:34
 */
class CamControlNode(camCon: CamControl, mot: Motion) extends Node {




  def this() = this(new CamControl(Triggers(Key._A),Triggers(Key._W),Triggers(Key._D),Triggers(Key._S),Triggers(), Triggers(), Triggers(), Triggers()), new Motion(Vec3f(0,0,0), 1))

}
