package main.scala.components

/**
 * Created by Eike
 * 23.03.14.
 */

import main.scala.math.{Mat4f, Mat3f, Vec3f}
import scala.xml.Node
import main.scala.architecture.{ComponentCreator, Component}
import main.scala.tools.Identifier


object Motion extends ComponentCreator {
  override def fromXML(xml: Node): Option[Motion] = xmlToComp[Motion](xml, "motion", n => {
    //val dir = n \ "direction"
    //val vel = n \ "velocity"
    //val fri: Float = (n \ "friction").text.toString.toFloat

    //val dirVec = Vec3f((dir \ "@x").text.toFloat,(dir \ "@y").text.toFloat,(dir \ "@z").text.toFloat)
    //val velVec = Vec3f((vel \ "@x").text.toFloat,(vel \ "@y").text.toFloat,(vel \ "@z").text.toFloat)
    //TODO: adjust
    Some(new Motion())
  })
}

class Motion() extends Component {

  final val sleepEpsilon: Float = 0.3f
  private var _canSleep: Boolean = false
  private var _isAwake: Boolean = true
  private var motion: Float = 0f

  private var _forceAccum: Vec3f = Vec3f(0,0,0)
  private var _torqueAccum: Vec3f = Vec3f(0,0,0)

  private var _velocity: Vec3f = Vec3f()
  private var _angularVelocity: Vec3f = Vec3f()
  private var _angularAcceleration: Vec3f = Vec3f()

  private var _acceleration: Vec3f = Vec3f()
  private var _gravity: Vec3f = Vec3f()
  var lastFrameAcceleration: Vec3f = Vec3f()



  private var _inverseMass: Float = 0f


  private var _inverseInertiaTensor: Mat3f = Mat3f()
  private var _inverseInertiaTensorWorld: Mat3f = Mat3f()

  private var _transformMatrix: Mat4f = Mat4f()

  private var _linearDamping: Float = 0f
  private var _angularDamping: Float = 0f

  def canSleep: Boolean = _canSleep
  def canSleep_=(cs: Boolean) = _canSleep = cs

  def isAwake: Boolean = _isAwake
  def awake(awake: Boolean) {
    if (awake) {
      _isAwake= true

      // Add a bit of motion to avoid it falling asleep immediately.
      motion = sleepEpsilon*2.0f
    } else {
      _isAwake = false
      velocity = Vec3f()
      angularVelocity = Vec3f()
    }
  }


  //The new mass of the body in kg. This may not be zero.
  def mass: Float = inverseMass match {
    case 0.0f => Float.MaxValue
    case _ => 1.0f/inverseMass
  }
  def mass_=(m: Float) = {
    assert(m != 0)
    inverseMass = 1.0f/m
  }
  def inverseMass: Float = _inverseMass
  protected def inverseMass_=(im: Float) = _inverseMass = im



  // inertia tensor
  def inertia: Mat3f = _inverseInertiaTensor.inverse()
  def inertia_=(iT: Mat3f) = {
    inverseInertia.setInverse(iT)
  }
  def inverseInertia:Mat3f = _inverseInertiaTensor

  def inverseInertiaTensorWorld: Mat3f = _inverseInertiaTensorWorld
  def inverseInertiaTensorWorld_=(iTW: Mat3f) = _inverseInertiaTensorWorld = iTW

  def transformMatrix: Mat4f = _transformMatrix
  def transformMatrix_=(tm: Mat4f) = _transformMatrix = tm

  // damping
  // usually between 0-1 - good value around 0.99f
  def linearDamping = _linearDamping
  // usually between 0-1 - good value around 0.8f
  def angularDamping = _angularDamping
  def damping_=(linearDamping: Float, angularDamping: Float) = {
    _linearDamping = linearDamping
    _angularDamping = angularDamping
  }


  //linear velocity as vector
  def velocity: Vec3f = _velocity
  def velocity_=(v: Vec3f) = _velocity = v


  // angular velocity, or rotation, or the rigid body
  def angularVelocity: Vec3f = _angularVelocity
  def angularVelocity_=(aV:Vec3f) = _angularVelocity = aV


  /**
   * acceleration
   * This value can be used to set acceleration due to gravity (its primary
   * use), or any other constant acceleration.
   * @return
   */
  def acceleration: Vec3f = _acceleration
  def acceleration_=(a: Vec3f) = _acceleration = a

  def angularAcceleration: Vec3f = _angularAcceleration
  def angularAcceleration_=(aA: Vec3f) = _angularAcceleration = aA

  /*
   * gravitational acceleration
   */
  def gravity: Vec3f = _gravity
  def gravity_=(g: Vec3f) = _gravity = g


  /**
   * Holds the accumulated force to be applied at the next
   * integration step.
   */
  def forceAccum: Vec3f = _forceAccum
  def forceAccum_=(f: Vec3f) = _forceAccum = f



  def addForce(f: Vec3f) {forceAccum += f}
  def addForceAtBodyPoint(force: Vec3f, point: Vec3f, position: Vec3f) {
    // Convert to coordinates relative to the center of mass.
    val pt: Vec3f = getPointInWorldSpace(point)
    addForceAtPoint(force, pt, position)
  }

  def addForceAtPoint(force: Vec3f, point: Vec3f, position: Vec3f)
  {
    // Convert to coordinates relative to center of mass.
    forceAccum += force
    torqueAccum += (point - position) crossProduct force

    _isAwake = true
  }

  def clearForceAccumulators() {
    forceAccum = Vec3f()
    torqueAccum = Vec3f()
  }

  /**
   * Holds the accumulated torque to be applied at the next
   * integration step.
   */
  def torqueAccum: Vec3f = _torqueAccum
  def torqueAccum_=(t: Vec3f) = _torqueAccum = t


  def hasFiniteMass = inverseMass >= 0.0f


  // Update the kinetic energy store, and possibly put the body to
  // sleep.
  def checkSleepState(duration: Double) {
    if (canSleep) {
      val currentMotion: Float = velocity ° velocity + angularVelocity ° angularVelocity

      val bias: Float = math.pow(0.5, duration).toFloat
      motion = bias*motion + (1-bias)*currentMotion

      if (motion < sleepEpsilon) _isAwake = false
      else if (motion > 10 * sleepEpsilon) motion = 10 * sleepEpsilon
    }
  }

  def getPointInWorldSpace(point: Vec3f): Vec3f = transformMatrix * point

  //TODO complete!
  override def toXML: Node = {
    <motion>

      <velocity x={velocity.x.toString} y={velocity.y.toString} z={velocity.z.toString} />

    </motion>
  }

  override def newInstance(i:Identifier): Component = new Motion
}
