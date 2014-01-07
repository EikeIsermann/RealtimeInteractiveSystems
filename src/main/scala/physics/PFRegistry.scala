package main.scala.physics

import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 07.01.14 11:04
 */


case class PFPair(particle: Particle, particleForce: ParticleForce) extends Tuple2(particle, particleForce)

class PFPairVector() extends mutable.ArrayBuffer[PFPair] {
  def make_pair(p: Particle, pf: ParticleForce)= new PFPair(p, pf)
}


class PFRegistry extends PFPairVector {

  def add(p: Particle, pf: ParticleForce) {
    add(make_pair(p, pf))
  }

  def add(pair: PFPair) {
    +=(pair)
  }

  def remove(pair: PFPair) {
    -=(pair)
  }

}