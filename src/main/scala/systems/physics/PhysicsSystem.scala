package main.scala.systems.physics


import main.scala.architecture.{Component, System}
import main.scala.systems.input.Context

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:31
 */
class PhysicsSystem extends System {
  /**
   * init the system
   * @return
   */
  override def init(): System = ???

  override def update(context: Context): Unit = ???

  override def -=(component: Component): System = ???

  override def +=(component: Component): System = ???

}
