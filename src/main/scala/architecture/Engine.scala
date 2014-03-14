package main.scala.architecture

import main.scala.input.{SimulationContext, Context}
import akka.actor.Actor
import main.scala.physics.PhysicsSystem

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:16
 */
trait Engine {

  def init()

  def += (system: System): Engine
  def -= (system: System): Engine

  def update(systemType: Class[_ <: System], context: Context)
  def mainLoop(): Unit

}
