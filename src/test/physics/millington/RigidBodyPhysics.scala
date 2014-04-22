package test.physics.millington

import main.scala.math._

/**
 * Created by Christian Treffs
 * Date: 07.04.14 15:49
 */
object RigidBodyPhysics {






}

class RigidBodyPhysics {

  /*
  * Definition of the sleep epsilon
  */
  final val sleepEpsilon: Float = 0.3f

  private var _forceAccum: Vec3f = Vec3f(0,0,0)
  private var _torqueAccum: Vec3f = Vec3f(0,0,0)
  private var _position: Vec3f = Vec3f(0,0,0)
  private var _rotation: Vec3f = Vec3f(0,0,0)
  private var _velocity: Vec3f = Vec3f(0,0,0)
  private var _acceleration: Vec3f = Vec3f(0,0,0)
  private var _inverseMass: Float = 0f
  private val _inverseInertiaTensor: Mat3f = Mat3f()

  private var _canSleep: Boolean = false
  /**
   * Holds the amount of damping applied to linear
   * motion.  Damping is required to remove energy added
   * through numerical instability in the integrator.
   */
  private var _linearDamping: Float = 0f
  /**
   * Holds the amount of damping applied to angular
   * motion.  Damping is required to remove energy added
   * through numerical instability in the integrator.
   */
  private var _angularDamping: Float = 0f

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
   * Holds the inverse of the mass of the rigid body. It is more
   * useful to hold the inverse mass because integration is simpler,
   * and because in real time simulation it is more useful to have
   * bodies with infinite mass (immovable) than zero mass (completely
   * unstable in numerical simulation).
   */
  def inverseMass: Float = _inverseMass
  protected def inverseMass_=(im: Float) = _inverseMass = im

  /**
   * Sets the intertia tensor for the rigid body.
   *
   * The inertia tensor for the rigid
   * body. This must be a full rank matrix and must be
   * invertible.
   *
   * This invalidates internal data for the rigid body.
   * Either an integration function, or the calculateInternals
   * function should be called before trying to get any settings
   * from the rigid body.
   */

  def inertiaTensor: Mat3f = ???
  def inertiaTensor_=(iT: Mat3f) = {
    inverseInertiaTensor.setInverse(iT)
  }

  /**
   * Holds the inverse of the body's inertia tensor. The
   * inertia tensor provided must not be degenerate
   * (that would mean the body had zero inertia for
   * spinning along one axis). As long as the tensor is
   * finite, it will be invertible. The inverse tensor
   * is used for similar reasons to the use of inverse
   * mass.
   *
   * The inertia tensor, unlike the other variables that
   * define a rigid body, is given in body space.
   *
   * @see inverseMass
   */
  def inverseInertiaTensor:Mat3f = _inverseInertiaTensor
  def inverseInertiaTensor_=(inertiaTensor: Mat3f) = {
    _inverseInertiaTensor.setInverse(inertiaTensor)
  }

  def linearDamping = _linearDamping
  def angularDamping = _angularDamping
  def damping_=(linearDamping: Float, angularDamping: Float) = {
    _linearDamping = linearDamping
    _angularDamping = angularDamping
  }

  /**
   * Holds the linear position of the rigid body in world space.
   */
  def position: Vec3f = _position
  def position_=(p: Vec3f) = _position = p
  /**
   * Holds the angular orientation of the rigid body in world space. */
  var orientation: Quat  = Quat()
  /**
   * Holds the linear velocity of the rigid body in world space. */
  def velocity: Vec3f = _velocity
  def velocity_=(v: Vec3f) = _velocity = v
  /**
   * Holds the angular velocity, or rotation, or the rigid body * in world space.
   */
  def rotation: Vec3f = _rotation
  def rotation_=(rotation:Vec3f) = _rotation = rotation


  def getInertiaTensorWorld(inertiaTensor: Mat3f): Mat3f = {
      inertiaTensor.setInverse(inverseInertiaTensorWorld)
   }

  def getInertiaTensorWorld: Mat3f = {
    val it: Mat3f = Mat3f()
      getInertiaTensorWorld(it)
      it
    }

  //DERIVED DATA //

  /**
   * Holds the inverse inertia tensor of the body in world
   * space. The inverse inertia tensor member is specified in
   * the body's local space.
   *
   * @see inverseInertiaTensor
   */
  var inverseInertiaTensorWorld: Mat3f = Mat3f()

  /**
   * Holds the amount of motion of the body. This is a recency
   * weighted mean that can be used to put a body to sleep.
   */
  var motion: Float = 0f

  /**
   * A body can be put to sleep to avoid it being updated
   * by the integration functions or affected by collisions
   * with the world.
   */
  var isAwake: Boolean = true

  /**
   * Some bodies may never be allowed to fall asleep.
   * User controlled bodies, for example, should be
   * always awake.
   */
  def canSleep: Boolean = _canSleep
  def canSleep_=(sleep: Boolean) = _canSleep = sleep

  /**
   * Holds a transform matrix for converting body space into
   * world space and vice versa. This can be achieved by calling
   * the getPointIn*Space functions.
   *
   * @see getPointInLocalSpace
   * @see getPointInWorldSpace
   * @see getTransform
   */
  var transformMatrix: Mat4f = Mat4f()

  /**
   * //Force and Torque Accumulators//
   *
   * These data members store the current force, torque and
   * acceleration of the rigid body. Forces can be added to the
   * rigid body in any order, and the class decomposes them into
   * their constituents, accumulating them for the next
   * simulation step. At the simulation step, the accelerations
   * are calculated and stored to be applied to the rigid body.
   */

  /**
   * Holds the accumulated force to be applied at the next
   * integration step.
   */
  def forceAccum: Vec3f = _forceAccum
  def forceAccum_=(f: Vec3f) = _forceAccum = f

  /**
   * Holds the accumulated torque to be applied at the next
   * integration step.
   */
  def torqueAccum: Vec3f = _torqueAccum
  def torqueAccum_=(t: Vec3f) = _torqueAccum = t

  /**
   * Holds the acceleration of the rigid body.  This value
   * can be used to set acceleration due to gravity (its primary
   * use), or any other constant acceleration.
   */
  def acceleration: Vec3f = _acceleration
  def acceleration_=(a: Vec3f) = _acceleration = a

  /**
   * Holds the linear acceleration of the rigid body, for the
   * previous frame.
   */
  var lastFrameAcceleration: Vec3f = Vec3f()



  /**
   * Returns true if the mass of the particle is not-infinite.
   */
  def hasFiniteMass = inverseMass >= 0.0f


  /**
   * Calculates internal data from state data. This should be called
   * after the body’s state is altered directly (it is called
   * automatically during integration). If you change the body’s
   * state and then intend to integrate before querying any data
   * (such as the transform matrix), then you can omit this step.
   */
  def calculateDerivedData()  {
    orientation = orientation.norm()

    // Calculate the transform matrix for the body.
    transformMatrix = Mat4f(position, orientation)

    // Calculate the inertiaTensor in world space.
    inverseInertiaTensorWorld = Mat4f.transformInertiaTensor(orientation,inverseInertiaTensor, transformMatrix)

  }


  
    /**
     * Adds the given force to the center of mass of the rigid body.
     * The force is expressed in world coordinates.
     *
     * @param force The force to apply.
     */
    def addForce(force: Vec3f) {
      forceAccum += force
    }

    def integrate(duration: Double)
    {
      if (!isAwake) return

      // Calculate linear acceleration from force inputs.
      // a2 = a0 + F/m
      lastFrameAcceleration = acceleration + forceAccum * inverseMass

      // Calculate angular acceleration from torque inputs.
      val angularAcceleration: Vec3f = inverseInertiaTensorWorld * torqueAccum

      // Adjust velocities
      // Update linear velocity from both acceleration and impulse.
      // v2 = v0 + a*t
      velocity = velocity + lastFrameAcceleration * duration

      // Update angular velocity from both acceleration and impulse.
      rotation = rotation + angularAcceleration * duration

      // Impose drag.
      velocity = velocity * math.pow(linearDamping, duration)
      rotation = rotation * math.pow(angularDamping, duration)

      // Adjust positions
      // Update linear position.
      position = position + velocity * duration

      // Update angular position.
      orientation = orientation.addScaledVector(rotation,duration.toFloat)

      // Normalise the orientation, and update the matrices with the new
      // position and orientation
      calculateDerivedData()

      // Clear accumulators.
      clearAccumulators()

      // Update the kinetic energy store, and possibly put the body to
      // sleep.
      if (canSleep) {
        val currentMotion: Float = velocity ° velocity + rotation ° rotation

        val bias: Float = math.pow(0.5, duration).toFloat
        motion = bias*motion + (1-bias)*currentMotion

        if (motion < sleepEpsilon) awake(awake = false)
        else if (motion > 10 * sleepEpsilon) motion = 10 * sleepEpsilon
      }
    }

    def awake(awake: Boolean) {
      if (awake) {
        isAwake= true

        // Add a bit of motion to avoid it falling asleep immediately.
        motion = sleepEpsilon*2.0f
      } else {
        isAwake = false
        velocity = Vec3f()
        rotation = Vec3f()
      }
    }

    def clearAccumulators()
    {
      forceAccum = Vec3f()
      torqueAccum = Vec3f()
    }

    /**
     * Adds the given force to the given point on the rigid body.
     * The direction of the force is given in world coordinates,
     * but the application point is given in body space. This is
     * useful for spring forces, or other forces fixed to the * body.
     *
     * @param force The force to apply.
     *
     * @param point The location at which to apply the force, in * body coordinates.
     */
    def addForceAtBodyPoint(force: Vec3f, point: Vec3f) {
      // Convert to coordinates relative to the center of mass.
      val pt: Vec3f = getPointInWorldSpace(point)
      addForceAtPoint(force, pt)
    }

    def addForceAtPoint(force: Vec3f, point: Vec3f)
    {
      // Convert to coordinates relative to center of mass.

      forceAccum += force
      torqueAccum += (point - position) crossProduct force

      isAwake = true
    }

    def getPointInWorldSpace(point: Vec3f): Vec3f = transformMatrix * point


  
}

