package main.scala.tools

import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 01.04.14 17:32
 */
object phy {

  final val gravityConstant: Float = 9.81f // 9.80665 m/s

  implicit def acceleration(F: Vec3f, m: Float): Vec3f = F / m // a = F/m
  implicit def acceleration(v1: Vec3f, t1: Long, v0: Vec3f, t0: Long): Vec3f = v1-v0 / (t1-t0).toFloat // a = dv/dt

  implicit def force(m: Float, a: Vec3f): Vec3f = m * a // F = m*a
  implicit def gForce(m: Float, g: Vec3f = Vec3f(0,0,gravityConstant)): Vec3f = m * g // G = m * g

  implicit def velocity(x1: Vec3f, t1: Long, x0: Vec3f, t0: Long): Vec3f = x1-x0/(t1-t0).toFloat // v = dx/dt


  def ns2sec(t: Long): Long = t/1000000000
  def ms2sec(t: Long): Long = t/1000
}
