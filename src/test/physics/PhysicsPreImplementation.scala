package test.physics

import main.scala.tools.{DC, phy}
import main.scala.math.{Quat, Vec3f}

/**
 * Created by Christian Treffs
 * Date: 03.04.14 11:16
 */

class PhysicsPreImplementation {

  private var currentPhyState: PhysicsState = null
  private var previousPhyState: PhysicsState = null
  private var accT0, accT1, accDT: Double = -1
  private var dt: Double = -1
  private var t: Double = -1
  private var alpha: Double = -1
  private var acc: Double = -1
  private val maxAccDT: Double = 0.25

  var tick: Int = 60

  def init(p: Vec3f = Vec3f(1.5f, 0.0f,0.0f), v: Vec3f = Vec3f(30.0f, 40.0f,0.0f), acc1: Vec3f = Vec3f(20.0f, 0.0f,0.0f), m: Float = 200.0f, damp: Float = 0.99f) {

    accT0 = phy.timeInSeconds()
    dt = 1d/tick.toDouble
    t = 0
    acc = 0


    currentPhyState = new PhysicsState(p, Quat(),acc1, m,Vec3f(),1)


    previousPhyState = new PhysicsState(p, Quat(),acc1, m,Vec3f(),1)

    println(previousPhyState, currentPhyState)
    DC.log("Physics System", "initialized")

  }

  // the main update
  //TODO: dt from game loop/engine?
  def update(delta: Double): PhysicsState =  {


    accDT = delta
    if(accDT > maxAccDT) {accDT = maxAccDT}
    acc += accDT
    
    while (acc >= dt) {

      //println(previousPhyState+" <== "+ currentPhyState)
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