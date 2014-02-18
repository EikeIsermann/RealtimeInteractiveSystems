package main.scala.world.entities

import main.scala.input.SimulationContext
import main.scala.tools.DC

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:25
 */
class SimulationRegistry extends EntityRegistry {

  DC.log("SimulationRegistry", this)

  def initAll(context: SimulationContext): Unit = entities.foreach(_.init(context))

  def simulateAll(context: SimulationContext): Unit = entities.foreach(_.simulate(context))

  def renderAll(context: SimulationContext): Unit = entities.foreach(_.render(context))
}
