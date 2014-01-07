package main.scala.physics

/**
 * Created by Christian Treffs
 * Date: 07.01.14 11:04
 */
trait ParticleForce {

  def updateForce(particle: Particle, deltaT: Long): Unit

}
