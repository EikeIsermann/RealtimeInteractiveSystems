package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:46
 */
object Gun extends ComponentCreator {
  override def fromXML(xml: Node): Option[Gun] = {
    xmlToComp[Gun](xml, "gun", n => {

      val lTP: Long = (n \ "lifetimeProjectile").text.toLong
      val cd: Long = (n \ "coolDown").text.toLong
      val tLS: Long = (n \ "timeOfLastShot").text.toLong
      val pCP: Float = (n \ "pitchConstraint" \ "@positive").text.toFloat
      val pCN: Float = (n \ "pitchConstraint" \ "@negative").text.toFloat
      val yC: Float = (n \ "yawConstraint").text.toFloat
      val sh: Boolean = (n \ "shoot").text.toBoolean
      val pro: Symbol = Symbol((n \ "projectile").text.toString)
      val pw: Float = (n \ "power").text.toFloat

      Some(new Gun(lTP,cd,tLS,pCP,pCN,yC,sh,pro,pw))
    })
  }
}

case class Gun(lifetimeProjectile1: Long = 0, coolDown1: Long = 1000, timeOfLastShot1: Long = 0, pCP: Float = 45f, pCN: Float = -10f, yC: Float = 180f, sh: Boolean = false, pro: Symbol = 'Bullet, pw: Float=30000) extends Component {

  val fireAngle = 10f

  private var _lifetimeProjectile: Long = lifetimeProjectile1
  private var _coolDown: Long = coolDown1
  private var _timeOfLastShot: Long = timeOfLastShot1
  private var _pitchConstraintPositive: Float = pCP
  private var _pitchConstraintNegative: Float = pCN
  private var _yawConstraint: Float = yC
  private var _shoot: Boolean = sh
  private var _projectile: Symbol = pro
  private var _power: Float = pw



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
    <gun>
       <lifetimeProjectile>{_lifetimeProjectile.toString}</lifetimeProjectile>
       <coolDown>{_coolDown.toString}</coolDown>
       <timeOfLastShot>{_timeOfLastShot.toString}</timeOfLastShot>
       <pitchConstraint positive={_pitchConstraintPositive.toString} negative={_pitchConstraintNegative.toString} />
       <yawConstraint>{_yawConstraint.toString}</yawConstraint>
       <shoot>{_shoot.toString}</shoot>
       <projectile>{_projectile.name.toString}</projectile>
       <power>{_power.toString}</power>
    </gun>
  }

  override def newInstance(i:Identifier): Component = new Gun(lifetimeProjectile,coolDown,timeOfLastShot)
}
