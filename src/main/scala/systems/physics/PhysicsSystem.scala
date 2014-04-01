package main.scala.systems.physics

import main.scala.architecture._
import main.scala.systems.input.SimulationContext
import main.scala.engine.GameEngine
import main.scala.nodes.PhysicsNode
import main.scala.components._
import main.scala.math.Vec3f
import main.scala.tools.phy

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:18
 *
 * PhysicsSystem
 * calculates forces and applies them to the game object
 *
 * http://buildnewgames.com/gamephysics/
 */
class PhysicsSystem extends System {

  override def init(): System = ???

  override def update(context: SimulationContext): System = {

    val physicsNodes: Seq[PhysicsNode] = Seq()//GameEngine.getNodeList(new PhysicsNode())

    val deltaT: Float = context.deltaT

    physicsNodes.foreach(pn => {

      val motion: Motion = pn.motion
      val placement: Placement = pn.placement
      val physics: Physics = pn.physics

      val gForce: Vec3f = physics.gForce // force that always is applied downwards (has only z Component)

      val inertia: Float = physics.inertia
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



    this
  }

  override def deinit(): Unit = ???


}
