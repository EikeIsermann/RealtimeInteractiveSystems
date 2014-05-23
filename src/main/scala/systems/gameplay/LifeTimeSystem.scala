package main.scala.systems.gameplay

import main.scala.architecture.{System, Node, ProcessingSystem}
import main.scala.nodes.LifeTimeNode
import main.scala.components.LifeTime
import main.scala.engine.GameEngine
import main.scala.tools.{phy, Sys, DC}

/**
 * Created by Christian Treffs
 * Date: 23.05.14 09:17
 */
class LifeTimeSystem extends ProcessingSystem {
  override var node: Class[_ <: Node] = classOf[LifeTimeNode]
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

  var currentTime: Long = 0


  override def processNode(node: Node): Unit = {
    node match {
      case lTN: LifeTimeNode =>
        val lifeTime: LifeTime = lTN -> classOf[LifeTime]
        lifeTime -= (ctx.deltaT*1000f).toLong

        if(lifeTime.timeLeft <= 0) {
          val e = GameEngine.entities(lifeTime.owner.toString)
          DC.log("Time's up",e)
          e.destroy()
        }
      case _ =>
    }

  }


  /**
   * executed after nodes are processed - every update
   */
  override def end(): Unit = {

  }
  /**
   * called on system shutdown
   */
  override def deinit(): Unit = {}

}
