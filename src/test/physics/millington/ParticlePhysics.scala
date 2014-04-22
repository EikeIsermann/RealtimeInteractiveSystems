package test.physics.millington

import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 04.04.14 14:17
 */
object ParticlePhysics {

  def main(args: Array[String]) {

    val a = Vec3f(1.23f, 2.34f, 3.45f)
    val s = 3.6788f
    val b = Vec3f(9.765f, 4.56f, 3.45f)


    println((a + b*s).inline)
  }

}

class ParticlePhysics() {

  def init(p: Vec3f = Vec3f(1.5f, 0.0f,0.0f), v: Vec3f = Vec3f(30.0f, 40.0f,0.0f), acc: Vec3f = Vec3f(20.0f, 0.0f,0.0f), m: Float = 200.0f, damp: Float = 0.99f) {
    position = p
    velocity = v
    acceleration = acc
    mass = m
    damping = damp

    clearAccumulator()
  }

  def update(dt: Double) {
    integrate(dt)
  }

  def deinit() {

  }


  private var _forceAccum: Vec3f = Vec3f(0,0,0)
  private var _position: Vec3f = Vec3f(0,0,0)
  private var _velocity: Vec3f = Vec3f(0,0,0)
  private var _acceleration: Vec3f = Vec3f(0,0,0)
  private var _inverseMass: Float = -1f
  private var _damping: Float = -1f

  /** Holds the linear position of the particle in world space. */
  def position: Vec3f = _position
  def position_=(p: Vec3f) = _position = p

  /**  Holds the linear velocity of the particle in world space. */
  def velocity: Vec3f = _velocity
  def velocity_=(v: Vec3f) = _velocity = v



  /** Holds the acceleration of the particle.  This value can be used to set acceleration due to gravity (its primary use), or any other constant acceleration. */
  def acceleration: Vec3f = _acceleration
  def acceleration_=(a: Vec3f) = _acceleration = a

  /** Holds the accumulated force to be applied at the next simulation iteration only. This value is zeroed at each integration step. */
  def forceAccum: Vec3f = _forceAccum
  def forceAccum_=(f: Vec3f) = _forceAccum = f


  /** The new mass of the body. This may not be zero. Small masses can produce unstable rigid bodies under simulation.
   * This invalidates internal data for the particle. Either an integration function, or the calculateInternals
   * function should be called before trying to get any settings from the particle. */
   def mass: Float = inverseMass match {
      case 0.0f => Float.MaxValue
      case _ => 1.0f/inverseMass
   }
   def mass_=(m: Float) = {
    assert(m != 0)
    inverseMass = 1.0f/m
  }


  /**
   * Holds the inverse of the mass of the particle. It
   * is more useful to hold the inverse mass because
   * integration is simpler, and because in real time
   * simulation it is more useful to have objects with
   * infinite mass (immovable) than zero mass
   * (completely unstable in numerical simulation).
   */
  def inverseMass: Float = _inverseMass
  def inverseMass_=(im: Float) = _inverseMass = im

  /** Holds the amount of damping applied to linear motion. Damping is required to remove energy added through numerical instability in the integrator. */
  def damping: Float = _damping
  def damping_=(d: Float) = _damping = d

  /**
   * Integrates the particle forward in time by the given amount.
   * This function uses a Newton-Euler integration method, which is a
   * linear approximation to the correct integral. For this reason it
   * may be inaccurate in some cases.
   */
  def integrate(dt: Double)  {
    // We don't integrate things with zero mass.
    if (inverseMass <= 0.0f) {
      return
    }

      assert(dt > 0.0)

    // Update linear position.
   position = position + velocity*dt

    // Work out the acceleration from the force
    val resultingAcc: Vec3f = acceleration + forceAccum * inverseMass


    // Update linear velocity from the acceleration.
   velocity = velocity + resultingAcc*dt

    // Impose drag.
    velocity = velocity * math.pow(damping, dt)

    // Clear the forces.
    clearAccumulator()
  }

  /**
   * Returns true if the mass of the particle is not-infinite.
   */
  def hasFiniteMass = inverseMass >= 0.0f

  /**
   * Clears the forces applied to the particle. This will be
   * called automatically after each integration step.
   */
  def clearAccumulator() = {
    forceAccum = Vec3f(0,0,0)
  }

  /**
   * Adds the given force to the particle, to be applied at the
   * next iteration only.
   *
   * @param force The force to apply.
   */
  def addForce(force: Vec3f) = {
    forceAccum += force
  }




}
