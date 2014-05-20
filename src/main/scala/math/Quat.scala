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

  implicit def Quat(mat4f: Mat4f): Quat = new Quat().apply(mat4f)
}

final case class Quat(private val x1: Float = 0,private val y1: Float = 0,private val z1: Float = 0,private val w1: Float = 1) extends QuaternionImp(x1,y1,z1,w1) {


  implicit def apply(mat4f: Mat4f): Quat = new QuaternionImp(mat4f)

  implicit def *(q1: Quat): Quat = mul(q1)
  implicit def *(d: Double): Quat = this.*(d.toFloat)
  implicit def *(f: Float): Quat = Quat(x1*f, y1*f, z1*f,w1*f)





  implicit def +(q1: Quat): Quat = Quat(x + q1.x, y + q1.y, z + q1.z, w + q1.w)
  implicit def -(q1: Quat): Quat = Quat(x - q1.x, y - q1.y, z - q1.z, w - q1.w)

  def norm(): Quat = normalize()


  /**
   * Adds the given v to this, scaled by the given amount.
   * This is used to update the orientation quaternion by a rotation
   * and time.
   *
   * @param vector The v to add.
   *
   * @param scale The amount of the v to add.
   */
  def addScaledVector(vector: Vec3f, scale: Float): Quat = {
    val q: Quat = scale*Quat(vector.x, vector.y, vector.z, 0)
    //q *= this //??
    Quat(x + q.x * 0.5f, y + q.y * 0.5f, z + q.z * 0.5f, w + q.w * 0.5f)
  }
}