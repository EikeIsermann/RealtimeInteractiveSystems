package main.scala.world.entities

import main.scala.input.SimulationContext

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:36
 */
class SimpleBallisticWeapon extends Entity {
  /**
   * get the entity's name
   * @return the name
   */
  def name: String = ???

  /**
   * creation and initialisation process
   */
  def init(context: SimulationContext): Boolean = ???

  /**
   * physics simulation process
   */
  def simulate(context: SimulationContext): Boolean = ???

  /**
   * rendering process
   */
  def render(context: SimulationContext): Boolean = ???

  /**
   * destruction process
   */
  def destroy(): Boolean = ???

  /**
   * get the lifetime of the entity
   * lifetime <= 0 equals infinite lifetime
   * @return lifetime in milliseconds
   */
  def lifetime: Long = ???
}
