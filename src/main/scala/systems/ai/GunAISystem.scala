package main.scala.systems.ai

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.GunAINode
import main.scala.components.{GunAI, Gun}

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:22
 * This is a RIS Project class
 */
class GunAISystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[GunAINode]
  override var priority: Int = _



  /**
   * called on system startup
   * @return
   */
  override def init(): System = {this}


  /**
   * executed before nodes are processed - every update
   */
  override def begin(): Unit = {}

  override def processNode(node: Node): Unit = {

    node match {
      case gN: GunAINode =>
        val ai: GunAI = gN -> classOf[GunAI]
        ai.state = ai.state.execute(gN)
      case _ =>
    }

  }

  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}
  /**
   * executed after nodes are processed - every update
   */
  override def end(): Unit = {}


}
