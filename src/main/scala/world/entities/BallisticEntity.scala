package main.scala.world.entities

import main.scala.physics.Particle
import main.scala.input.SimulationContext

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:19
 */
class BallisticEntity extends Entity with Particle {

  def init(context: SimulationContext): Boolean = ???

  def simulate(context: SimulationContext): Boolean = {
    // physics simulation

    false
  }

  def render(context: SimulationContext): Boolean = {
    // translate
    // colorize

    false
  }

  def exit(): Boolean = ???


  def getEntityRegistry: EntityRegistry = ???

  def setEntityRegistry: EntityRegistry = ???
}
