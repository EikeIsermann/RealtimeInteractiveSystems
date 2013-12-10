package main.scala.world.entities

import main.scala.input.SimulationContext

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:36
 */
class Floor extends Entity {
  def init(context: SimulationContext): Boolean = ???

  /**
   * physics simulation process
   * @param context
   */
  def simulate(context: SimulationContext): Boolean = ???

  /**
   * rendering process
   * @param context
   */
  def render(context: SimulationContext): Boolean = ???

  def exit(): Boolean = ???

  def getEntityRegistry: EntityRegistry = ???

  def setEntityRegistry: EntityRegistry = ???
}
