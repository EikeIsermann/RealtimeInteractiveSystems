package main.scala.math

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 07.04.14 16:34
 */
object Mat3f {

  implicit def apply() = new Mat3f()

}

class Mat3f(m00: Float = 1, m01: Float = 0, m02: Float = 0, m10: Float = 0, m11: Float = 1, m12: Float = 0, m20: Float = 0, m21: Float = 0, m22: Float = 1) extends {

  private val values: ArrayBuffer[Float] = ArrayBuffer(m00,m01,m02,m10,m11,m12,m20,m21,m22)

  def apply(index: Int) = values(index)


  implicit def *(vector: Vec3f): Vec3f = new Vec3f(
    vector.x * apply(0) + vector.y * apply(1) + vector.z * apply(2),
    vector.x * apply(3) + vector.y * apply(4) + vector.z * apply(5),
    vector.x * apply(6) + vector.y * apply(7) + vector.z * apply(8)
   )

  /**
   * Sets the matrix to be the inverse of the given matrix.
   *
   * @param m The matrix to invert and use to set this.
   */
  def setInverse(m: Mat3f): Mat3f = {
    val t4 = m(0)*m(4)
    val t6 = m(0)*m(5)
    val t8 = m(1)*m(3)
    val t10 = m(2)*m(3)
    val t12 = m(1)*m(6)
    val t14 = m(2)*m(6)

    // Calculate the determinant
    val t16 = t4 * m(8) - t6 * m(7) - t8 * m(8) + t10 * m(7) + t12 * m(5) - t14 * m(4)

    // Make sure the determinant is non-zero.
    assert(t16 != 0.0f)
    val t17 = 1/t16


    values(0) = (m(4)*m(8)-m(5)*m(7))*t17
    values(1) = -(m(1)*m(8)-m(2)*m(7))*t17
    values(2) = (m(1)*m(5)-m(2)*m(4))*t17
    values(3) = -(m(3)*m(8)-m(5)*m(6))*t17
    values(4) = (m(0)*m(8)-t14)*t17
    values(5) = -(t6-t10)*t17
    values(6) = (m(3)*m(7)-m(4)*m(6))*t17
    values(7) = -(m(0)*m(7)-t12)*t17
    values(8) = (t4-t8)*t17

    this
  }

  /** Returns a new matrix containing the inverse of this matrix. */
  def inverse(): Mat3f = {
    val res = new Mat3f()
    res.setInverse(this)
    res
  }

  /**
   * Inverts the matrix
   */
  def invert(): Unit = setInverse(this)

  /**
   * Sets the value of the matrix from inertia tensor values.
   */
  def setInertiaTensorCoeffs(ix: Float, iy: Float, iz: Float, ixy: Float = 0f, ixz:Float = 0f, iyz:Float = 0f) {
    values(0) = ix
    values(1) = -ixy
    values(2) = -ixz
    values(3) = -ixy
    values(4) = iy
    values(5) = -iyz
    values(6) = -ixz
    values(7) = -iyz
    values(8) = iz
  }

}
