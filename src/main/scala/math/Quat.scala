package main.scala.math

import ogl.vecmathimp.QuaternionImp

/**
 * Created by Christian Treffs
 * Date: 02.04.14 13:28
 */
object Quat {

  def zero: Quat = new Quat(0,0,0,0)
  def identity: Quat = new Quat()
  implicit def Quat(quat: ogl.vecmath.Quaternion) = new Quat(quat.x(), quat.y(), quat.z(), quat.w())
}

final case class Quat(private val x1: Float = 0,private val y1: Float = 0,private val z1: Float = 0,private val w1: Float = 1) extends QuaternionImp(x1,y1,z1,w1) {

  implicit def *(q1: Quat): Quat = mul(q1)
  implicit def *(d: Double): Quat = this.*(d.toFloat)
  implicit def *(f: Float): Quat = Quat(x1*f, y1*f, z1*f,w1*f)



  implicit def +(q1: Quat): Quat = Quat(x + q1.x, y + q1.y, z + q1.z, w + q1.w)
  implicit def -(q1: Quat): Quat = Quat(x - q1.x, y - q1.y, z - q1.z, w - q1.w)

  def norm(): Quat = normalize()
}