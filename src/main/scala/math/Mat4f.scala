package main.scala.math


/**
 * Created by Christian Treffs
 * Date: 07.01.14 16:33
 */

object Mat4f {
  implicit def Mat4f(mat: ogl.vecmath.Matrix) = new Mat4f(mat.getValues)
}

final case class Mat4f(m00: Float = 1.0f,m01: Float = 0,m02: Float = 0,m03: Float = 0,m10: Float = 0,m11: Float = 1.0f,m12: Float = 0,m13: Float = 0,m20: Float = 0,m21: Float = 0,m22: Float = 1.0f,m23: Float = 0,m30: Float = 0,m31: Float = 0,m32: Float = 0,m33: Float = 1.0f) extends ogl.vecmathimp.MatrixImp(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 ) {

  def this(arr: Array[Float]) = this(arr(0), arr(1), arr(2), arr(3), arr(4), arr(5), arr(6), arr(7), arr(8), arr(9), arr(10), arr(11), arr(12), arr(13), arr(14), arr(15))


  def this(seq: Seq[Float]) = this(seq.toArray)


  def position: Vec3f   = getPosition
  def translation: Mat4f = getTranslation
  def rotation: Mat4f   = getRotation

  def values: Array[Float] = getValues


  def * (m: Mat4f) = mult(m)





}


object Test {



}