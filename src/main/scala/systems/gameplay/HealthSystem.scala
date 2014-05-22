package main.scala.systems.gameplay

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.HealthNode
import main.scala.components.Health
import main.scala.engine.GameEngine
import main.scala.tools.DC

/**
 * Created by Christian Treffs
 * Date: 22.05.14 14:31
 */
class HealthSystem extends ProcessingSystem{

  override var node: Class[_ <: Node] = classOf[HealthNode]
  override var priority: Int = 0

  /**
   * called on system startup
   * @return
   */
  override def init(): System = this


  /**
   * executed before nodes are processed - every update
   */
  override def begin(): Unit = {}


  override def processNode(node: Node): Unit = {
    node match {
      case hN: HealthNode =>
        val health = hN -> classOf[Health]
        if(health.health > health.maxHealth) {
          throw new IllegalArgumentException("health is grater than the maxHealth!")
        }

        if(health.health <= 0) {
          GameEngine.entities.get(owner.toString).map(e => {
            DC.log(e.toString,"killed because 0 health",3)
            e.destroy()
          })
        }

      case _ =>
    }
  }
  /**
   * executed after nodes are processed - every update
   */
  override def end(): Unit = {}

  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}



}
