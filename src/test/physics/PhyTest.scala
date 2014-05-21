package test.physics

import main.scala.math.{Mat3f, Vec3f}
import test.physics.millington.{RigidBodyPhysics, ParticlePhysics}
import main.scala.tools.phy
import main.scala.components.{Physics, Placement}

/**
 * Created by Christian Treffs
 * Date: 03.04.14 18:03
 */
object PhyTest {

  var forceToApply: Vec3f = Vec3f()

  def main(args: Array[String]) {

    val placement = new Placement()
    val motion = new Physics()




    val rk = new PhysicsPreImplementation
    val p = new ParticlePhysics()


    val body = new RigidBodyPhysics
    body.mass = 200.0f // 200.0kg
    body.velocity = Vec3f(40.0f, -100.0f, 0)
    body.acceleration = Vec3f(0.0f, 21.0f, 0.0f)
    body.damping_=(0.99f, 0.8f)
    val radius = 0.4f

    body.canSleep = true
    body.awake(awake = true)
    var tensor = new Mat3f()
    val coeff: Float = 0.4f*body.mass*radius*radius
    tensor.setInertiaTensorCoeffs(coeff,coeff,coeff)
    body.inertiaTensor = tensor

    // Set the data common to all particle types
    body.position = Vec3f(0.0f, 1.5f, 0.0f)
    //startTime = TimingData::get().lastFrameTimestamp

    // Clear the force accumulators
    body.calculateDerivedData()
    //calculateInternals();


    val w = new TestWindow()

    p.init(
      Vec3f(0,0,0),
      Vec3f(0,0,0),
      Vec3f(10,-10,0),
      1f, //kg
      0.93f //dampening
    )
    rk.init(
      Vec3f(0,0,0),
      Vec3f(0,0,0),
      Vec3f(3,-9.81f,0),
      1f, //kg
      0.93f //dampening
    )



    var c = 0



    var lastT = phy.timeInSeconds()
    var currentT = 0d
    while (true) {

      //val m = Vec3f(10, 0, 0)

      currentT = phy.timeInSeconds()

      val dt = currentT-lastT
      if(dt > 0.0) {
        p.update(dt)
        val rkPos = rk.update(dt)
        //println(s.position.inline)

        if(forceToApply != Vec3f()) {
          body.addForce(forceToApply)
        }

        if(body.lastFrameAcceleration != Vec3f(0.0f, 21.0f, 0.0f)) {
          println(body.lastFrameAcceleration.inline)
        }

        body.integrate(dt)
        //println(body.position.inline)

        w.update(Vec3f(),body.position)


      }
      lastT = currentT
      c += 1
    }

    p.deinit()
  }
}

/*object PhysicsSystem {
  def main(args: Array[String]) {
    val m = new Motion()
    val p = new Placement()

    m.mass = 200.0f
    m.velocity =  Vec3f(40.0f, -100.0f, 0)
    m.acceleration =  Vec3f(0.0f, 21.0f, 0.0f)
    m.damping_=(0.99f, 0.8f)
    val tensor = new Mat3f()
    val coeff: Float = 0.064f*m.mass
    tensor.setInertiaTensorCoeffs(coeff,coeff,coeff)
    m.inertia = tensor
    m.canSleep = true
    m.awake(awake = true)


    p.position = Vec3f(0.0f, 1.5f, 0.0f)


    val ps = PhysicsSystem.newInstance(m,p)
    ps.init()

    val w = new TestWindow()

    var lastT = phy.timeInSeconds()
    var currentT = 0d
    while(!w.quit) {

      currentT = phy.timeInSeconds()

      val dt = currentT-lastT
      if(dt > 0.0) {
        ps.integrate(dt)
        w.update(Vec3f(),p.position)
      }
      lastT = currentT
    }

    ps.deinit()




  }
  def newInstance(m1: Motion, p1: Placement): PhysicsSystem = {
    val ps = new PhysicsSystem
    ps.m = m1
    ps.p = p1
    ps
  }
}    */

