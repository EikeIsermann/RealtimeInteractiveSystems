package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:46
 */
case class Gun(lifetimeProjectile1: Long = 0, coolDown1: Long = 100, timeOfLastShot1: Long = 0) extends Component {
  private var _lifetimeProjectile: Long = lifetimeProjectile1
  private var _coolDown: Long = coolDown1
  private var _timeOfLastShot: Long = timeOfLastShot1
  private var _pitchConstraintPositive: Float = 45f
  private var _pitchConstraintNegative: Float = -10f
  private var _yawConstraint: Float = 180f
  private var _shoot: Boolean = false
  private var _projectile: Symbol = 'Bullet
  private var _power: Float = 30000

  def power = _power
  def power_=(f:Float) = _power = f

  def pitchConstraintPositive: Float = _pitchConstraintPositive
  def pitchConstraintPositive_=(f: Float) = _pitchConstraintPositive = f

  def shoot(s: Boolean) = {_shoot = s}
  def shoot = _shoot

  def projectile = _projectile
  def projectile_=(p: Symbol) = _projectile = p


  def pitchConstraintNegative: Float = _pitchConstraintNegative
  def pitchConstraintNegative_=(f: Float) = _pitchConstraintNegative = f

  def yawConstraint: Float = _yawConstraint
  def yawConstraint_=(f:Float) = _yawConstraint = f

  def lifetimeProjectile: Long = _lifetimeProjectile
  def lifetimeProjectile_=(t: Long) = _lifetimeProjectile = t

  def coolDown: Long = _coolDown
  def coolDown_=(t: Long) = _coolDown = t

  def timeOfLastShot: Long = _timeOfLastShot
  def timeOfLastShot_=(t: Long) = _timeOfLastShot = t


  override def toXML: Node = {
    null
  }

  override def newInstance(i:Identifier): Component = new Gun(lifetimeProjectile,coolDown,timeOfLastShot)
}
