package main.scala.world.entities

import main.scala.input.SimulationContext

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:35
 */
class Camera extends Entity{
  def init(context: SimulationContext): Boolean = {false}

  /**
   * physics simulation process
   * @param context
   */
  def simulate(context: SimulationContext): Boolean = {false}

  /**
   * rendering process
   * @param context
   */
  def render(context: SimulationContext): Boolean = {false}

  def exit(): Boolean = {false}

  def getEntityRegistry: EntityRegistry = {null}

  def setEntityRegistry: EntityRegistry = {null}
}
