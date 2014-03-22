package main.scala.math

import ogl.vecmathimp.{MatrixImp, FactoryDefault}


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

case class Mat4f(m00: Float = 1.0f,m01: Float = 0,m02: Float = 0,m03: Float = 0,m10: Float = 0,m11: Float = 1.0f,m12: Float = 0,m13: Float = 0,m20: Float = 0,m21: Float = 0,m22: Float = 1.0f,m23: Float = 0,m30: Float = 0,m31: Float = 0,m32: Float = 0,m33: Float = 1.0f) extends ogl.vecmathimp.MatrixImp(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 ) {

  def this(arr: Array[Float]) = this(arr(0), arr(1), arr(2), arr(3), arr(4), arr(5), arr(6), arr(7), arr(8), arr(9), arr(10), arr(11), arr(12), arr(13), arr(14), arr(15))
  def this(seq: Seq[Float]) = this(seq.toArray)


  implicit def position: Vec3f   = getPosition
  implicit def translation: Mat4f = getTranslation
  implicit def rotation: Mat4f   = getRotation

  implicit def values: Array[Float] = getValues

  implicit def + (m: Mat4f): Mat4f = Mat4f(
      m00+m.m00, m01+m.m01, m02+m.m02, m03+m.m03,
      m10+m.m10, m11+m.m11, m12+m.m12, m13+m.m13,
      m20+m.m20, m21+m.m21, m22+m.m22, m23+m.m23,
      m30+m.m30, m31+m.m31, m32+m.m32, m33+m.m33
    )

  implicit def - (m: Mat4f): Mat4f = Mat4f(
    m00-m.m00, m01-m.m01, m02-m.m02, m03-m.m03,
    m10-m.m10, m11-m.m11, m12-m.m12, m13-m.m13,
    m20-m.m20, m21-m.m21, m22-m.m22, m23-m.m23,
    m30-m.m30, m31-m.m31, m32-m.m32, m33-m.m33
  )
  //TODO: this might be confusion!!!! B*A != A*B => octave checked: A * B = A* B now but is it mult right?
  implicit def * (m: Mat4f): Mat4f = m.mult(this)
  implicit def / (m: Mat4f): Mat4f = m.inv * this





  /**
   * the inverse matrix = get the matrix that multiplied with the original matrix equals the identity matrix
   * @return inverse matrix
   */
  def inv: Mat4f = inverse
  def `-1`: Mat4f = inverse
  def inverse: Mat4f = invertFull()

  def inverseRigid: Mat4f = invertRigid()

  /**
   * the transposed matrix = the matrix that is mirrored along the diagonal axis
   * @return
   */

  //def ':Mat4f = transposed

  def T: Mat4f = transposed
  def trans: Mat4f = transposed
  def transposed: Mat4f = transpose()


  /**
   * CAUTION!!!! different from original to String method!
   */
  override def toString: String = {
    "Mat4f:\n"+
    this.transpose().toString
  }
}


// identity Matrix short
object I {
  def apply(): Mat4f = Mat4f.identity
}

object Test {

  def main(args: Array[String]) {
    val A = Mat4f(1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ,11, 12, 13, 14, 15, 16)
    val B = Mat4f(17, 18, 19, 20, 21, 22, 23, 24, 25, 26 ,27, 28, 29, 30, 31, 32)

    val AplusB = Mat4f(18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 42, 44, 46, 48)
    val AminusB = Mat4f(-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16,-16)
    val AmultB = Mat4f( 250, 260,270,280,618,    644,    670,    696,    986,   1028,   1070,   1112,    1354,   1412,   1470,   1528)
    //val AdivB = Mat4f(1.9000e+00,8.0000e-01,-3.0000e-01,-1.4000e+00,1.6000e+00,7.0000e-01,-2.0000e-01,-1.1000e+00,1.3000e+00,6.0000e-01,-1.0000e-01,-8.0000e-01,1.0000e+00,5.0000e-01,9.8810e-15,-5.0000e-01)


    val Atransposed = Mat4f( 1,5,    9,   13, 2,   6,   10,   14, 3,    7,   11,   15, 4,    8,   12,   16)

    // addition
    println("add - true: "+(A + B == AplusB))
    println("add - false: "+(A + B.transposed == AplusB))

    // subtraction - works
    println("sub - true: "+(A - B == AminusB))
    println("sub - false: "+(A - B.transposed == AminusB))


    // multiplication - works
    println("mult - true: "+(A * B == AmultB))
    println("mult - false: "+(B * A == AmultB))

    // identity check - works
    println("ident - true: "+ (A * I() == I() * A))
    println("ident - true: "+ (A * I() == A))
    println("ident - true: "+ (I() * A == A))

    // transposed - works
    println("transpose - true: "+(A.T == Atransposed))
    println("transpose - false: "+(A.T.T == Atransposed))

    //TODO: inverse full has a problem!!!
    //println(B.inverse)


  }


}