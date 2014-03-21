package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import main.scala.math.Vec3f
import main.scala.systems.gfx.{Shader, Mesh}
import scala.xml.NodeSeq

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:08
 */
object Component {

  def main(args: Array[String]) {

    val a = new Ammo(10, 100)
    val b = new Ammo(10, 100)
    println(
      a.identifier,
      b.identifier
    )
  }



}











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