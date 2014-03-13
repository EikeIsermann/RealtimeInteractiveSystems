package main.scala.math

import ogl.vecmathimp.FactoryDefault


/**
 * Created by Christian Treffs
 * Date: 07.01.14 16:33
 */

object Mat4f {
  implicit def apply(mat: ogl.vecmath.Matrix) = new Mat4f(mat.getValues)
  implicit def apply(arr: Array[Float]) = new Mat4f(arr)
  implicit def apply(seq: Seq[Float]) = new Mat4f(seq)

  final val identity: Mat4f = FactoryDefault.vecmath.identityMatrix()

  def projection(fovy: Float, aspect: Float, zNear: Float, zFar: Float): Mat4f = FactoryDefault.vecmath.perspectiveMatrix(fovy, aspect, zNear, zFar)

  def translation(t: Vec3f): Mat4f = FactoryDefault.vecmath.translationMatrix(t)
  def translation(x: Float, y: Float, z: Float): Mat4f = FactoryDefault.vecmath.translationMatrix(x, y, z)

  def rotation(r: Vec3f, angle: Float): Mat4f = FactoryDefault.vecmath.rotationMatrix(r, angle)
  def rotation(x: Float, y: Float, z: Float, angle: Float): Mat4f = FactoryDefault.vecmath.rotationMatrix(x, y, z, angle)

  def scale(v: Vec3f): Mat4f = FactoryDefault.vecmath.scaleMatrix(v)
  def scale(x: Float, y: Float, z: Float): Mat4f = FactoryDefault.vecmath.scaleMatrix(x,y,z)
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