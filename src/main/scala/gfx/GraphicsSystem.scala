package main.scala.gfx


import main.scala.architecture._
import main.scala.input.Context

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:33
 */
class GraphicsSystem extends System {
  override def update(context: Context): Unit = ???

  override def -=(component: Component): System = ???

  override def +=(component: Component): System = ???

  /**
   * init the system
   * @return
   */
  override def init(): System = ???
}
