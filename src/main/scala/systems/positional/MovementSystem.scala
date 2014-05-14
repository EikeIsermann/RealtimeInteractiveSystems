package main.scala.systems.positional

import main.scala.architecture._
import main.scala.systems.input.SimulationContext
import main.scala.engine.GameEngine
import main.scala.nodes.MovementNode
import main.scala.components.{Placement, Motion}

import main.scala.tools.DC
import main.scala.math.Vec3f

/**
 * Created by Eike
 * 22.03.14.
 */
class MovementSystem extends System {

  def update(context: SimulationContext): System = {
    val nodes = GameEngine.getNodeList(new MovementNode)

    for (node <- nodes) {
      val motion = node -> classOf[Motion]
      val pos = node -> classOf[Placement]

      pos.position = motion.velocity
      motion.velocity = Vec3f(0, 0, 0)

      //TODO: different!
      //pos.rotation = motion.direction
      //motion.direction = Vec3f(0,0,0)

    }

    this
  }

   def init(): System = {
    DC.log("Movement System", "initialized", 2)
    this
  }

   def deinit(): Unit = {
    DC.log("Control System", "ended", 2)
  }

  override var node: Class[_ <: Node] = _

  override def update(): System = ???

  override def begin(): Unit = ???

  override def end(): Unit = ???

  override var priority: Int = _
}