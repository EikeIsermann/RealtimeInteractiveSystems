package main.scala.components

import main.scala.architecture.Component
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:08
 */

class Position(x1: Float, y1: Float, z1: Float) extends Component {
  def this(pos: Vec3f) = this(pos.x, pos.y, pos.z)
  private val _vector: Vec3f = Vec3f(x1, y1, z1)
  def x = _vector.x
  def y = _vector.y
  def z = _vector.z
  def vector: Vec3f = _vector
}

class Display extends Component


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