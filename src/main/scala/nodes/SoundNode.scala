package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Sound, Motion, Placement}


/**
 * Created by Christian Treffs
 * Date: 19.05.14 10:55
 */
class SoundNode(sound: Sound, placement: Placement, motion: Motion) extends Node(sound,placement,motion) {
  def this() = this(new Sound(),new Placement(),new Motion())
}
