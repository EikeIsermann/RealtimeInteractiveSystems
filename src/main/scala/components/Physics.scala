package main.scala.components

import main.scala.math.{Mat4f, Mat3f, Vec3f}
import scala.xml.Node
import main.scala.architecture.{ComponentCreator, Component}
import main.scala.tools.Identifier


object Physics extends ComponentCreator {
  override def fromXML(xml: Node): Option[Physics] = {
    xmlToComp[Physics](xml, "physics", n => {
      val ma = n \ "mass"
      val gra = n \ "gravity"
      val vel = n \ "velocity"
      val acc = n \ "acceleration"
      val damp = n \ "damping"
      val ine = n \ "inertia"

      val mass = ma.text.toFloat
      val gravity = Vec3f((gra \ "@x").text.toFloat,(gra \ "@y").text.toFloat,(gra \ "@z").text.toFloat )
      val velocity = Vec3f((vel \ "@x").text.toFloat,(vel \ "@y").text.toFloat,(vel \ "@z").text.toFloat )
      val acceleration = Vec3f((acc \ "@x").text.toFloat,(acc \ "@y").text.toFloat,(acc \ "@z").text.toFloat )
      val linearDampening = (damp \ "linear").text.toFloat
      val angularDampening = (damp \ "angular").text.toFloat



      val inertia: Mat3f = {
        val arr = ine.text.split(" ")
        val aF = arr.map(_.toFloat)
        val m = new Mat3f()
        for(i <- 0 until aF.length) {
          m.values(i) = aF(i)
        }
        m
      }
      val phy = new Physics(velocity,acceleration,mass,linearDampening,inertia,angularDampening,gravity)

      phy.isSolid = (n \ "isSolid").text.toBoolean
      phy.canSleep = (n \ "canSleep").text.toBoolean
      phy.awake((n \ "awake").text.toBoolean)
      Some(phy)
    })
  }
}

class Physics(velocity1: Vec3f = Vec3f(), acceleration1: Vec3f = Vec3f(), mass1: Float = 1f, linearDampening1: Float = 0.99f, inertia1: Mat3f = Mat3f(), angularDampening1: Float = 0.8f, gravity1: Vec3f = Vec3f(0,-9.81f,0)) extends Component {

  var isSolid: Boolean = false

  final val sleepEpsilon: Float = 0.3f

  private var _velocity: Vec3f = null
  private var _gravity: Vec3f = null
  private var _acceleration: Vec3f = null
  private var _inverseMass: Float = -1f

  private var _angularVelocity: Vec3f = Vec3f(0,0,0)
  private var _angularAcceleration: Vec3f = Vec3f()


  private var _transformMatrix: Mat4f = Mat4f()
  private var _canSleep: Boolean = true
  private var _isAwake: Boolean = true
  private var motion: Float = 0f

  private val _inverseInertiaTensor: Mat3f = Mat3f()
  private var _inverseInertiaTensorWorld: Mat3f = Mat3f()
  private var _forceAccum: Vec3f = Vec3f(0,0,0)
  private var _torqueAccum: Vec3f = Vec3f(0,0,0)
  private var _linearDamping: Float = linearDampening1
  private var _angularDamping: Float = angularDampening1

  var lastFrameAcceleration: Vec3f = Vec3f()
  var testRot: Vec3f = Vec3f(0,0,0)


  mass_=(mass1)
  gravity_=(gravity1)
  velocity_=(velocity1)
  acceleration_=(acceleration1)
  damping_=(linearDampening1,angularDampening1)
  //inertia_=(inertia1)
  canSleep = true
  awake(awake = true)


  //TODO: tensor and so on

  /*val tensor = new Mat3f()
  val coeff: Float = 0.064f*mass
  println("COEFF:"+coeff)
  tensor.setInertiaTensorCoeffs(coeff,coeff,coeff)
  inertia_=(tensor)  */


  def hasFiniteMass = inverseMass >= 0.0f





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






  // Update the kinetic energy store, and possibly put the body to
  // sleep.
  def checkSleepState(duration: Double) {

    if (canSleep) {                                        //TODO: angular
      val currentMotion: Float = (velocity ° velocity) //+ (angularVelocity ° angularVelocity)

      val bias: Float = math.pow(0.5, duration).toFloat
      motion = bias*motion + (1-bias)*currentMotion


      if (motion < sleepEpsilon){


        _isAwake = false
      }
      else if (motion > 10 * sleepEpsilon){

        motion = 10 * sleepEpsilon
      }
    }
  }

  def getPointInWorldSpace(point: Vec3f): Vec3f = transformMatrix * point


  def canSleep: Boolean = _canSleep
  def canSleep_=(cs: Boolean): Boolean =  {_canSleep = cs; _canSleep}

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
    inverseMass
  }
  def inverseMass: Float = _inverseMass
  def inverseMass_=(im: Float) = _inverseMass = im



  // inertia tensor
  def inertia: Mat3f = _inverseInertiaTensor.inverse()
  def inertia_=(iT: Mat3f) = {
    inverseInertia.setInverse(iT)
  }
  def inverseInertia:Mat3f = _inverseInertiaTensor

  def inverseInertiaTensorWorld: Mat3f = _inverseInertiaTensorWorld
  def inverseInertiaTensorWorld_=(iTW: Mat3f) = _inverseInertiaTensorWorld = iTW






  //linear velocity as vector
  def velocity: Vec3f = _velocity
  def velocity_=(v: Vec3f) = {
    _velocity = v
  }


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
  def acceleration_=(a: Vec3f) = {
    _acceleration = a
  }

  def angularAcceleration: Vec3f = _angularAcceleration
  def angularAcceleration_=(aA: Vec3f) = _angularAcceleration = aA

  override def newInstance(identifier: Identifier): Component = new Physics(velocity,acceleration,mass,linearDamping,inertia,angularDamping,gravity)

  override def toXML: Node = {
    <physics>
      <mass>{mass.toString}</mass>
      <gravity x={gravity.x.toString} y={gravity.y.toString} z={gravity.z.toString} />
      <velocity x={velocity.x.toString} y={velocity.y.toString} z={velocity.z.toString} />
      <acceleration x={acceleration.x.toString} y={acceleration.y.toString} z={acceleration.z.toString} />
      <damping>
        <linear>{linearDamping.toString}</linear>
        <angular>{linearDamping.toString}</angular>
      </damping>
      <forceAccum x={forceAccum.x.toString} y={forceAccum.y.toString} z={forceAccum.z.toString} />
      <inertia>{inertiaToXML()}</inertia>
      <canSleep>{canSleep.toString}</canSleep>
      <awake>{isAwake.toString}</awake>
      <isSolid>{isSolid.toString}</isSolid>
    </physics>
  }

  def inertiaToXML():String = {
    val sb = new StringBuilder()

    inertia.values.map(v => sb.append(v+" "))

    sb.toString().trim
  }
}
