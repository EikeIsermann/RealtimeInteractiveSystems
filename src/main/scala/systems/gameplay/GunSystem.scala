package main.scala.systems.gameplay

import main.scala.architecture.{System, Node, ProcessingSystem}

/**
 * User: uni
 * Date: 20.05.14
 * Time: 16:06
 * This is a RIS Project class
 */
class GunSystem extends ProcessingSystem {
  var node: Class[_ <: Node] = _
  var priority: Int = _

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
