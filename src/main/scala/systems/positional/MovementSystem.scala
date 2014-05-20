package main.scala.systems.positional

import main.scala.architecture._
import main.scala.nodes.MovementNode
import main.scala.components.{Physics, Placement}
import main.scala.math.Vec3f

/**
 * Created by Eike
 * 22.03.14.
 */
class MovementSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[MovementNode]
  override var priority: Int = 0


  override def begin(): Unit = {}

  override def end(): Unit = {}

  override def processNode(n: Node): Unit = {
    n match {
      case movNode: MovementNode =>

        val motion = movNode -> classOf[Physics]
        val placement = movNode -> classOf[Placement]

        placement.position = motion.velocity
        motion.velocity = Vec3f(0, 0, 0)

        //TODO: different!
        placement.rotation = motion.testRot
        motion.testRot = Vec3f(0,0,0)
      case _ => throw new IllegalArgumentException("not a valid node")
    }
  }


  def init(): System = {
    this
  }

  def deinit(): Unit = {
  }


  /* def update(context: SimulationContext): System = {
     val nodes = GameEngine.getNodeList(new MovementNode)

     for (node <- nodes) {


     }

     this
   }   */


}