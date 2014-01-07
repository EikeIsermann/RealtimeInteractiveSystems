package main.scala.physics

/**
 * Created by Christian Treffs
 * Date: 07.01.14 11:41
 */
class PFBuoyancy extends ParticleForce {
  def updateForce(particle: Particle, deltaT: Long): Unit = {

    particle.addForce()
  }
}
