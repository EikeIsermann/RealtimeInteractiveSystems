package main.scala

import main.scala.input.SimulationContext
import main.scala.entities._

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:33
 */
class SimpleBallisticDemo {

  val context: SimulationContext = null

  val entities: SimulationRegistry = null

  entities.addEntity(new Camera())
  entities.addEntity(new Floor())
  entities.addEntity(new SimpleBallisticWeapon())



  entities.initAll(context)
  
  context.updateDeltaT()

  entities.simulateAll(context)

  entities.renderAll(context)



}
