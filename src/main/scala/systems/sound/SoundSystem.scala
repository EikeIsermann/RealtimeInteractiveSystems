package main.scala.systems.sound

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.SoundNode
import main.scala.components.{Physics, Placement, Sound}

/**
 * Created by Christian Treffs
 * Date: 19.05.14 10:53
 */
class SoundSystem extends ProcessingSystem {
  override var node: Class[_ <: Node] = classOf[SoundNode]
  override var priority: Int = 0

  override def init(): System = {
    Audio.init()
    // load sounds
    Audio.load()

    this
  }

  override def begin(): Unit = {}

  override def processNode(node: Node): Unit = {
    node match {
      case soundNode: SoundNode =>
        val sound: Sound = soundNode -> classOf[Sound]
        val placement: Placement = soundNode -> classOf[Placement]
        val motion: Physics = soundNode -> classOf[Physics]


        sound.position_=(placement.position)
        sound.velocity_=(motion.velocity)


        sound.play()


        sound.playList.clear()

      case _ => throw new IllegalArgumentException("can not process node "+node)
    }
  }

  override def end(): Unit = {

  }

  override def deinit(): Unit = {
    Audio.deinit()
  }
}
