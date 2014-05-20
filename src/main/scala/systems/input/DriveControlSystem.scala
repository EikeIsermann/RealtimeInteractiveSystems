package main.scala.systems.input

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.DriveControlNode

/**
 * User: uni
 * Date: 19.05.14
 * Time: 19:06
 * This is a RIS Project class
 */
class DriveControlSystem extends ProcessingSystem {
  var node: Class[_ <: Node] = classOf[DriveControlNode]
  var priority: Int = 1

  /**
   * called on system startup
   * @return
   */
  def init(): System = ???

  /**
   * executed before nodes are processed - every update
   */
  def begin(): Unit = ???

  /**
   * executed after nodes are processed - every update
   */
  def end(): Unit = ???

  /**
   * called on system shutdown
   */
  def deinit(): Unit = ???

  def processNode(node: Node): Unit = ???
}
