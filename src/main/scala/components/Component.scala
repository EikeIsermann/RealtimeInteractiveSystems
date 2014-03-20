package main.scala.components

import main.scala.architecture.Component
import main.scala.math.Vec3f
import main.scala.systems.gfx.{Shader, Mesh}
import main.scala.entities.Entity

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:08
 */
object Component {}

case class Position(x1: Float, y1: Float, z1: Float) extends Component {
  def this(pos: Vec3f) = this(pos.x, pos.y, pos.z)

  private var _vector: Vec3f = Vec3f(x1, y1, z1)

  def vec: Vec3f = _vector
  def vec_=(v: Vec3f) = _vector = v
}

case class Display(mesh1: Mesh, shader1: Shader) extends Component {
  private var _mesh: Mesh = mesh1
  private var _shader: Shader = shader1

  def mesh: Mesh = _mesh
  def mesh_=(m: Mesh) = _mesh = m

  def shader: Shader = _shader
  def shader_=(s: Shader) = _shader = s
}

case class Ammo(ammo1: Int, maxAmmo1: Int) extends Component {
  private var _ammo: Int = ammo1
  private var _maxAmmo: Int = maxAmmo1

  def ammo: Int = _ammo
  def ammo_=(a: Int) = _ammo = a

  def maxAmmo: Int = _maxAmmo
  def maxAmmo_=(a: Int) = _maxAmmo = a
}

case class Gun(lifetimeProjectile1: Long, coolDown1: Long, timeOfLastShot1: Long = 0) extends Component {
  private var _lifetimeProjectile: Long = lifetimeProjectile1
  private var _coolDown: Long = coolDown1
  private var _timeOfLastShot: Long = timeOfLastShot1

  def lifetimeProjectile: Long = _lifetimeProjectile
  def lifetimeProjectile_=(t: Long) = _lifetimeProjectile = t

  def coolDown: Long = _coolDown
  def coolDown_=(t: Long) = _coolDown = t

  def timeOfLastShot: Long = _timeOfLastShot
  def timeOfLastShot_=(t: Long) = _timeOfLastShot = t
}

case class Projectile(damage1: Float) extends Component {
  private var _damage: Float = damage1
  def damage: Float = _damage
  def damage_=(d: Float) = _damage = d
}

case class Health(health1: Int, maxHealth1: Int) extends Component {
  private var _health: Int = health1
  private var _maxHealth: Int = maxHealth1

  def health: Int = _health
  def health_=(h: Int) = _health = h

  def maxHealth: Int = _maxHealth
  def maxHealth_=(mh: Int) = _maxHealth = mh
}

case class LifeTime() extends Component

case class Camera(fieldOfView: Float, active: Boolean = true) extends Component

case class Light() extends Component

case class KeyControl() extends Component

case class MouseControl() extends Component
/**
Bullet
  damage
  range

Display
  object
  layer

Enemy
  prey

Gun
  timeSinceLastShot
  bulletLifetime

Hero (marker class)

HeroControl
  leftKey, rightKey, upKey, downKey
  attackKey, runKey
  rotationSpeed

Life
  health, maxHealth
  stamina, maxStamina

Motion
  velocityX, velocityY
  friction

Position
  x, y, rotation

Predator
  prey

Prey
  predator

Stalker
  prey
  */