package main.scala.world.entities

import main.scala.input.SimulationContext

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:25
 */
class SimulationRegistry extends EntityRegistry {

  def initAll(context: SimulationContext): Boolean = execute(_.init(context))
  def simulateAll(context: SimulationContext): Boolean = execute(_.simulate(context))
  def renderAll(context: SimulationContext): Boolean = execute(_.render(context))
  def exitAll(): Boolean = execute(_.exit())

}
