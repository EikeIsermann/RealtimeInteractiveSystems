package main.scala.systems.physics

import main.scala.architecture._
import main.scala.components._
import main.scala.math.Mat4f
import main.scala.nodes.PhysicsNode

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:18
 *
 * PhysicsSystem
 * calculates forces and applies them to the game object
 */



class PhysicsSystem(simSpeed: Int) extends IntervalProcessingSystem {

  override var acc: Float = 0
  override var priority = 0
  override val interval: Float = 1f/simSpeed.toFloat
  override var node: Class[_ <: Node] = classOf[PhysicsNode]

  def init(): System = {
    println(this,"interval:"+interval)
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
    m.lastFrameAcceleration = m.acceleration + m.forceAccum * m.inverseMass

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




  def deinit(): Unit = {}
}
