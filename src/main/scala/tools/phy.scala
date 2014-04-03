package main.scala.tools

import main.scala.math.{Quat, Vec3f}

/**
 * Created by Christian Treffs
 * Date: 01.04.14 17:32
 */
object phy {

  final val epsilon = 0.00001f ///< floating point epsilon for single precision
  final val gravityConstant: Float = 9.81f // 9.80665 m/s

  implicit def acceleration(F: Vec3f, m: Float): Vec3f = F / m // a = F/m
  implicit def acceleration(v1: Vec3f, t1: Long, v0: Vec3f, t0: Long): Vec3f = v1-v0 / (t1-t0).toFloat // a = dv/dt

  implicit def force(m: Float, a: Vec3f): Vec3f = m * a // F = m*a
  implicit def gForce(m: Float, g: Vec3f = Vec3f(0,0,gravityConstant)): Vec3f = m * g // G = m * g

  implicit def velocity(x1: Vec3f, t1: Long, x0: Vec3f, t0: Long): Vec3f = x1-x0/(t1-t0).toFloat // v = dx/dt

  def timeInSeconds(): Double = ns2sec(System.nanoTime())

  def ns2sec(t: Long): Double = t.toDouble/1000000000d
  def ms2sec(t: Long): Double = t.toDouble/1000d


  def slerp(a: Quat, b: Quat, t: Float): Quat = {
    assert(t>=0)
    assert(t<=1)

    var flip: Float = 1

    var cosine: Float = a.w*b.w + a.x*b.x + a.y*b.y + a.z*b.z

    if (cosine<0)
    {
      cosine = -cosine
      flip = -1
    }

    if ((1-cosine)<epsilon)
      return a * (1-t) + b * (t*flip)

    val theta: Float = math.acos(cosine).toFloat
    val sine: Float = math.sin(theta).toFloat
    val beta: Float = (math.sin((1-t)*theta) / sine).toFloat
    val alpha: Float = (math.sin(t*theta) / sine * flip).toFloat

    a * beta + b * alpha
  }


}
