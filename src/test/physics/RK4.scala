package test.physics

import main.scala.math.{Quat, Vec3f}
import main.scala.tools.phy

/**
 * Created by Christian Treffs
 * Date: 03.04.14 14:35
 */
object RK4 {

  def integrate(state: PhysicsState, t: Double, dt: Double) {



    val a: Derivative = evaluate(state, t)
    val b: Derivative = evaluate(state, t, dt*0.5f, a)
    val c: Derivative = evaluate(state, t, dt*0.5f, b)
    val d: Derivative = evaluate(state, t, dt, c)

    // dx = v*dt
    state.position        += (a.velocity + 2.0f * (b.velocity + c.velocity) + d.velocity) * 1.0f/6.0f * dt
    state.momentum        += (a.force + 2.0f * (b.force + c.force) + d.force) * 1.0f/6.0f * dt

    state.orientation     += (a.spin + ((b.spin + c.spin) * 2.0f) + d.spin)* (1.0f/6.0f) * dt
    state.angularMomentum += (a.torque + 2.0f * (b.torque + c.torque) + d.torque) * 1.0f/6.0f * dt

    state.recalculateSecondaryValues()

  }
  
  
  def evaluate(state: PhysicsState, t: Double): Derivative = applyForces(state, t)

  def evaluate(state: PhysicsState, t: Double, dt: Double, derivative: Derivative): Derivative = {
    // dx = v*dt
    state.position        += derivative.velocity  * dt
    //dp = F * dt
    state.momentum        += derivative.force     * dt

    state.orientation     += derivative.spin      * dt
    state.angularMomentum += derivative.torque    * dt
    state.recalculateSecondaryValues()


    applyForces(state, t+dt)
  }



  /// Calculate force and torque for physics state at time t.
  /// Due to the way that the RK4 integrator works we need to calculate
  /// force implicitly from state rather than explictly applying forces
  /// to the rigid body once per update. This is because the RK4 achieves
  /// its accuracy by detecting curvature in derivative values over the
  /// timestep so we need our force values to supply the curvature.

  def applyForces(state: PhysicsState, t: Double): Derivative = {

    //TODO: FORCES NEEED TO BEEEE ADJUSTED!!!!

    val damp = 1f
    val dampFac = math.pow(damp,t).toFloat

    val gravity: Vec3f = Vec3f(-0.1f,-0.2f) //TODO: adjust gravitantional factor to realistic value



    // F = m*a


    val force = Vec3f(
      dampFac*state.momentum.x ,
      dampFac*state.momentum.y+9.81f ,
      dampFac*state.momentum.z
    )







    /*val force = Vec3f(
      state.position.x /*+ math.sin(t*0.9f + 0.5f).toFloat * 10f*/,
      state.position.y /*+ math.sin(t*0.5f + 0.4f).toFloat * 11f*/,
      state.position.z /*+  math.sin(t*0.7f + 0.9f).toFloat * 12f*/
    ) */


    // sine torque to get some spinning action
    // damping torque so we don't spin too fast
    val torque: Vec3f = Vec3f(
      /*1.0f * math.sin(t * 0.9f + 0.5f).toFloat*/- 0.2f * state.angularVelocity.x,
      /*1.1f * math.sin(t * 0.5f + 0.4f).toFloat*/- 0.2f * state.angularVelocity.y,
      /*1.2f * math.sin(t * 0.7f + 0.9f).toFloat*/ - 0.2f * state.angularVelocity.z
    )


    //println("F:",force.inline,"T:",torque.inline)

    new Derivative(state.velocity, force, state.spin, torque)
  }


  def interpolate(a: PhysicsState, b: PhysicsState, alpha: Float): PhysicsState = {
    val state = b
    state.position = a.position*(1-alpha) + b.position*alpha
    state.momentum = a.momentum*(1-alpha) + b.momentum*alpha
    state.orientation =  phy.slerp(a.orientation, b.orientation, alpha)
    state.angularMomentum = a.angularMomentum*(1-alpha) + b.angularMomentum*alpha
    state.recalculateSecondaryValues()
    state
  }
  
}



/**
 * 
 * @param position the position of the cube center of mass in world coordinates (meters).
 * @param orientation the orientation of the cube represented by a unit quaternion.
 * @param momentum the momentum of the cube in kilogram meters per second.
 * @param mass mass of the cube in kilograms.
 * @param angularMomentum angular momentum v.
 * @param inertiaTensor inertia tensor of the cube (i have simplified it to a single value due to the mass properties a cube).
 */
class PhysicsState(
                         var position: Vec3f,
                         var orientation: Quat,
                         var momentum: Vec3f,
                         var mass: Float,
                         var angularMomentum: Vec3f,
                         // TODO: actual tensor?
                         var inertiaTensor: Float
                         ) {

  // secondary state

  var velocity: Vec3f = Vec3f()               ///< velocity in meters per second (calculated from momentum).
  var spin: Quat = Quat()                     ///< quaternion rate of change in orientation.
  var angularVelocity: Vec3f = Vec3f()        ///< angular velocity (calculated from angularMomentum).

  /// constant state

  var inverseMass: Float = 1.0f/mass              ///< inverse of the mass used to convert momentum to velocity.
  var inverseInertiaTensor: Float = 1.0f / inertiaTensor    ///< inverse inertia tensor used to convert angular momentum to angular velocity.

  /// Recalculate secondary state values from primary values.

  def recalculateSecondaryValues() {
    inverseMass = 1.0f/mass
    velocity = momentum * inverseMass
    angularVelocity = angularMomentum * inverseInertiaTensor
    orientation.norm()
    spin = Quat(angularVelocity.x, angularVelocity.y, angularVelocity.z, 0) * orientation * 0.5
  }

  override def toString = "PS: x:"+position.xyz+"\tp:"+momentum.xyz+"\tm:"+mass+"\tmi:"+inverseMass
}

/**
 *
 * @param velocity velocity is the derivative of position
 * @param force force in the derivative of momentum.
 * @param spin spin is the derivative of the orientation quaternion.
 * @param torque torque is the derivative of angular momentum.
 */
case class Derivative(
                       var velocity: Vec3f = Vec3f(),
                       var force: Vec3f= Vec3f(),
                       var spin: Quat=Quat(),
                       var torque: Vec3f= Vec3f()
                       )
