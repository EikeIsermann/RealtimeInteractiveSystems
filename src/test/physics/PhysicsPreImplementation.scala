package test.physics

import main.scala.tools.{DC, phy}
import main.scala.math.{Quat, Vec3f}

/**
 * Created by Christian Treffs
 * Date: 03.04.14 11:16
 */

object PhyTest {
  def main(args: Array[String]) {
    val p = new PhysicsPreImplementation

    p.init()

    var c = 0

    while (c < 1000) {
      val m = Vec3f(10,0,0)
      val s = p.update(m)

      println(s.position.inline)
      c += 1
    }

    p.deinit()
  }
}


class PhysicsPreImplementation {

  private var currentPhyState: PhysicsState = null
  private var previousPhyState: PhysicsState = null
  private var accT0, accT1, accDT: Double = -1
  private var dt: Double = -1
  private var t: Double = -1
  private var alpha: Double = -1
  private var acc: Double = -1
  private val maxAccDT: Double = 0.25

  def init(tick: Int = 60) {
    accT0 = phy.timeInSeconds()
    dt = 1d/tick.toDouble
    t = 0
    acc = 0
    currentPhyState = PhysicsState()
    previousPhyState = PhysicsState()

    DC.log("Physics System", "initialized")

  }

  // the main update
  //TODO: dt from game loop/engine?
  def update(m: Vec3f): PhysicsState=  {

    accT1 = phy.timeInSeconds()
    accDT = accT1 - accT0
    if(accDT > maxAccDT) {accDT = maxAccDT}
    accT0 = accT1
    acc += accDT


    currentPhyState.momentum = m
    
    while (acc >= dt) {
      previousPhyState = currentPhyState

      //TODO: extract component infos into physics state


      RK4.integrate(currentPhyState, t, dt)



      
      t += dt
      acc -= dt
    }
    
    alpha = acc / dt

    val res = RK4.interpolate(previousPhyState, currentPhyState, alpha.toFloat)
    //TODO: extract state info and put it into components


    res

  }

  def deinit() {

    DC.log("Physics System", "ended")
  }






}