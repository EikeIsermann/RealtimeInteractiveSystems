package main.scala.app

import main.scala.systems.input.SimulationContext
import main.scala.world.entities._

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:33
 */
object SimpleBallisticDemo {

  def main(args: Array[String]) {
  val context: SimulationContext = new SimulationContext

  val entities: SimulationRegistry = new SimulationRegistry

  //entities.addEntity(new Camera())
  //entities.addEntity(new Floor())
//  entities.addEntity(new SimpleBallisticWeapon())


  entities.initAll(context)
  
  context.updateDeltaT()

  entities.simulateAll(context)

  entities.renderAll(context)

  }

}
