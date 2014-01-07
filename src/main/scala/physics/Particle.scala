package main.scala.physics

import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:16
 */
trait Particle {

  private var currentForce: Vec3f

  def addForce(force: Vec3f) = currentForce = currentForce + force
  def removeForce(force: Vec3f) = currentForce = currentForce - force


}
