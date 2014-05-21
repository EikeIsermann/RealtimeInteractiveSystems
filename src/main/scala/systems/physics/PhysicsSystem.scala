package main.scala.systems.physics

import main.scala.architecture._
import main.scala.components._
import main.scala.math.{Vec3f, Mat4f}
import main.scala.nodes.PhysicsNode

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:18
 *
 * PhysicsSystem
 * calculates forces and applies them to the game object
 *
 * http://buildnewgames.com/gamephysics/
 */



class PhysicsSystem extends ProcessingSystem {

  override var node: Class[_ <: Node] = classOf[PhysicsNode]
  override var priority = 0



  def init(): System = {
    this
  }

  override def begin(): Unit = {}

  override def processNode(node: Node): Unit = {
      node match {
        case phyNode: PhysicsNode =>
          val motion: Physics = phyNode -> classOf[Physics]
          val placement: Placement = phyNode -> classOf[Placement]


          integrate(motion,placement,ctx.deltaT)
        case _ => throw new IllegalArgumentException("not the right node")
      }
  }


  override def end(): Unit = {}




  private def integrate(m: Physics, p: Placement, duration: Double) {
    if(!m.isAwake) return




    // Calculate linear acceleration from force inputs.
    // a2 = a0 + F/m
    m.lastFrameAcceleration = m.gravity + m.acceleration + m.forceAccum * m.inverseMass
    println(m.lastFrameAcceleration.inline)

    // Calculate angular acceleration from torque inputs.
    m.angularAcceleration = m.inverseInertiaTensorWorld * m.torqueAccum

    // Adjust velocities
    // Update linear velocity from both acceleration and impulse.
    // v2 = v0 + a*t
    m.velocity = m.velocity + m.lastFrameAcceleration * duration

    // Update angular velocity from both acceleration and impulse.
    m.angularVelocity = m.angularVelocity + m.angularAcceleration * duration

    // Impose drag.
    m.velocity            = m.velocity            * math.pow(m.linearDamping, duration)
    m.angularAcceleration = m.angularAcceleration * math.pow(m.angularDamping, duration)

    // Adjust positions
    // Update linear position.
    p.position = p.position + m.velocity * duration

    // Update angular position.
    p.orientation = p.orientation.addScaledVector(m.angularVelocity,duration.toFloat)

    // Normalise the orientation, and update the matrices with the new
    // position and orientation
      p.orientation = p.orientation.norm()

      // Calculate the transform matrix for the body.
      m.transformMatrix = Mat4f(p.position, p.orientation)

      // Calculate the inertiaTensor in world space.
      m.inverseInertiaTensorWorld = Mat4f.transformInertiaTensor(p.orientation,m.inverseInertia, m.transformMatrix)

    //clear accumulators
    m.clearForceAccumulators()

    // Update the kinetic energy store, and possibly put the body to
    // sleep.
    m.checkSleepState(duration)
  }




  def deinit(): Unit = {
  }


  /*override def update(context: SimulationContext): System = {
    integrate(context.deltaT)
    this
  }*/


  /*
 //DEPRECATED
 val physicsNodes: Seq[PhysicsNode] = Seq()//GameEngine.getNodeList(new PhysicsNode())


  val deltaT: Float = context.deltaT

  physicsNodes.foreach(pn => {

    val motion: Motion = pn.motion
    val placement: Placement = pn.placement
    val physics: Physics = pn.physics

    //val gForce: Vec3f = physics.gForce // force that always is applied downwards (has only z Component)

    //val inertia: Float = physics.inertia
    val mass: Float = physics.mass
    val position: Vec3f = placement.position
    val t1 = phy.ns2sec(System.nanoTime())
    val resultingForce: Vec3f = Vec3f() + phy.gForce(mass)

    // 1. Figure out what the forces are on an object

    // 2. Add those forces up to get a single “resultant” or “net” force
    val force = Vec3f() + phy.gForce(mass)

    //Use the object’s velocity to calculate the object’s position
    val accelerationLast: Vec3f = motion.acceleration
    placement.position +=  motion.velocity*deltaT.toFloat + (0.5f*accelerationLast*(deltaT*deltaT))

    // Use F = ma to calculate the object’s acceleration due to those forces
    val accelerationNew: Vec3f = force / mass
    val accelerationAverage: Vec3f = (accelerationLast + accelerationNew) / 2f

    // Use the object’s acceleration to calculate the object’s velocity
    motion.velocity += accelerationAverage * deltaT


  })


  /*

https://en.wikipedia.org/wiki/Verlet_integration#Velocity_Verlet
  last_acceleration = acceleration
position += velocity * time_step + ( 0.5 * last_acceleration * time_step^2 )
new_acceleration = force / mass
avg_acceleration = ( last_acceleration + new_acceleration ) / 2
velocity += avg_acceleration * time_step

   */


  /*
  acceleration = force / mass
  velocity += acceleration * time_step
  position += velocity * time_step

  Vector forces = 0.0f;

// gravity
forces += down * m_gravityConstant; // 9.8m/s/s on earth

// left/right movement
forces += right * m_movementConstant * controlInput; // where input is scaled -1..1

// add other forces in for taste - usual suspects include air resistence
// proportional to the square of velocity, against the direction of movement.
// this has the effect of capping max speed.

Vector acceleration = forces / m_massConstant;
m_velocity += acceleration * timeStep;
m_position += velocity * timeStep;
   */


  */

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