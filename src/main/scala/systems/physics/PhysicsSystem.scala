package main.scala.systems.physics


import main.scala.architecture.{Node, Component, System}
import main.scala.systems.input.Context

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:31
 */
class PhysicsSystem extends System {
  override def update(nodeType: Class[_ <: Node], context: Context): System = ???

  override def update(context: Context): System = ???

  override def init(): System = ???
}
