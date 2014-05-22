package main.scala.systems.ai

import main.scala.architecture.{System, Node, ProcessingSystem}

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:22
 * This is a RIS Project class
 */
class GunAISystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = _
  override var priority: Int = _

  override def processNode(node: Node): Unit = ???

  /**
   * called on system startup
   * @return
   */
  override def init(): System = ???

  /**
   * called on system shutdown
   */
  override def deinit(): Unit = ???

  /**
   * executed before nodes are processed - every update
   */
  override def begin(): Unit = ???

  /**
   * executed after nodes are processed - every update
   */
  override def end(): Unit = ???


}
