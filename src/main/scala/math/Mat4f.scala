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

  implicit def apply(position: Vec3f, orientation: Quat): Mat4f =
    new Mat4f(
    1-2*orientation.y*orientation.y- 2*orientation.z*orientation.z,
    2*orientation.x*orientation.y - 2*orientation.w*orientation.z,
    2*orientation.x*orientation.z + 2*orientation.w*orientation.y,
    position.x,

    2*orientation.x*orientation.y + 2*orientation.w*orientation.z,
    1-2*orientation.x*orientation.x- 2*orientation.z*orientation.z,
    2*orientation.y*orientation.z - 2*orientation.w*orientation.x,
    position.y,

    2*orientation.x*orientation.z - 2*orientation.w*orientation.y,
    2*orientation.y*orientation.z + 2*orientation.w*orientation.x,
    1-2*orientation.x*orientation.x- 2*orientation.y*orientation.y,
    position.z,

    0.0f, 0.0f, 0.0f, 1.0f

    )

  /**
   * Internal function to do an intertia tensor transform by a quaternion.
   * Note that the implementation of this function was created by an
   * automated code-generator and optimizer.
   */
  def transformInertiaTensor(q: Quat, iitBody: Mat3f, rotmat: Mat4f): Mat3f = {
    val t4  = rotmat(0)*iitBody(0)+rotmat(1)*iitBody(3)+rotmat(2)*iitBody(6)
    val t9  = rotmat(0)*iitBody(1)+rotmat(1)*iitBody(4)+rotmat(2)*iitBody(7)
    val t14 = rotmat(0)*iitBody(2)+rotmat(1)*iitBody(5)+rotmat(2)*iitBody(8)
    val t28 = rotmat(4)*iitBody(0)+rotmat(5)*iitBody(3)+rotmat(6)*iitBody(6)
    val t33 = rotmat(4)*iitBody(1)+rotmat(5)*iitBody(4)+rotmat(6)*iitBody(7)
    val t38 = rotmat(4)*iitBody(2)+rotmat(5)*iitBody(5)+rotmat(6)*iitBody(8)
    val t52 = rotmat(8)*iitBody(0)+rotmat(9)*iitBody(3)+rotmat(10)*iitBody(6)
    val t57 = rotmat(8)*iitBody(1)+rotmat(9)*iitBody(4)+rotmat(10)*iitBody(7)
    val t62 = rotmat(8)*iitBody(2)+rotmat(9)*iitBody(5)+rotmat(10)*iitBody(8)

    new Mat3f(
      t4*rotmat(0)+t9*rotmat(1)+t14*rotmat(2),
      t4*rotmat(4)+t9*rotmat(5)+t14*rotmat(6),
      t4*rotmat(8)+t9*rotmat(9)+t14*rotmat(10),
      t28*rotmat(0)+t33*rotmat(1)+t38*rotmat(2),
      t28*rotmat(4)+t33*rotmat(5)+t38*rotmat(6),
      t28*rotmat(8)+t33*rotmat(9)+t38*rotmat(10),
      t52*rotmat(0)+t57*rotmat(1)+t62*rotmat(2),
      t52*rotmat(4)+t57*rotmat(5)+t62*rotmat(6),
      t52*rotmat(8)+t57*rotmat(9)+t62*rotmat(10)
    )


  }


  final val identity: Mat4f = FactoryDefault.vecmath.identityMatrix()

  def projection(fovy: Float, aspect: Float, zNear: Float, zFar: Float): Mat4f = FactoryDefault.vecmath.perspectiveMatrix(fovy, aspect, zNear, zFar)

  def translation(t: Vec3f): Mat4f = FactoryDefault.vecmath.translationMatrix(t)
  def translation(x: Float, y: Float, z: Float): Mat4f = FactoryDefault.vecmath.translationMatrix(x, y, z)

  def rotation(r: Vec3f, angle: Float): Mat4f = FactoryDefault.vecmath.rotationMatrix(r, angle)
  def rotation(x: Float, y: Float, z: Float, angle: Float): Mat4f = FactoryDefault.vecmath.rotationMatrix(x, y, z, angle)

  def rotation(rotVec: Vec3f): Mat4f = Mat4f.rotation(1,0,0,rotVec.x()) * Mat4f.rotation(0,1,0,rotVec.y()) * Mat4f.rotation(0,0,1,rotVec.z())

  def scale(v: Vec3f): Mat4f = FactoryDefault.vecmath.scaleMatrix(v)
  def scale(x: Float, y: Float, z: Float): Mat4f = FactoryDefault.vecmath.scaleMatrix(x,y,z)
}

case class Mat4f(m00: Float = 1.0f,m01: Float = 0,m02: Float = 0,m03: Float = 0,m10: Float = 0,m11: Float = 1.0f,m12: Float = 0,m13: Float = 0,m20: Float = 0,m21: Float = 0,m22: Float = 1.0f,m23: Float = 0,m30: Float = 0,m31: Float = 0,m32: Float = 0,m33: Float = 1.0f) extends ogl.vecmathimp.MatrixImp(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33 ) {

  def this(arr: Array[Float]) = this(arr(0), arr(1), arr(2), arr(3), arr(4), arr(5), arr(6), arr(7), arr(8), arr(9), arr(10), arr(11), arr(12), arr(13), arr(14), arr(15))
  def this(seq: Seq[Float]) = this(seq.toArray)

  def apply(index: Int): Float = get((index.toFloat / 4f).toInt,index % 4)


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



  implicit def * (v: Vec3f): Vec3f = Vec3f(
      v.x * apply(0) +
      v.y * apply(1) +
      v.z * apply(2) + apply(3),

    v.x * apply(4) +
      v.y * apply(5) +
      v.z * apply(6) + apply(7),

    v.x * apply(8) +
      v.y * apply(9) +
      v.z * apply(10) + apply(11)
    )



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


  def det: Float = determinant
  def determinant: Float = { //method sig. takes a matrix (two dimensional array), returns determinant.
      m03 * m12 * m21 * m30 - m02 * m13 * m21 * m30-
      m03 * m11 * m22 * m30+m01 * m13 * m22 * m30+
      m02 * m11 * m23 * m30-m01 * m12 * m23 * m30-
      m03 * m12 * m20 * m31+m02 * m13 * m20 * m31+
      m03 * m10 * m22 * m31-m00 * m13 * m22 * m31-
      m02 * m10 * m23 * m31+m00 * m12 * m23 * m31+
      m03 * m11 * m20 * m32-m01 * m13 * m20 * m32-
      m03 * m10 * m21 * m32+m00 * m13 * m21 * m32+
      m01 * m10 * m23 * m32-m00 * m11 * m23 * m32-
      m02 * m11 * m20 * m33+m01 * m12 * m20 * m33+
      m02 * m10 * m21 * m33-m00 * m12 * m21 * m33-
      m01 * m10 * m22 * m33+m00 * m11 * m22 * m33
  }


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
    val C = Mat4f(1, 2 ,3 ,4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)


    println(A(0))
    println(A(1))
    println(A(2))
    println(A(3))
    println(A(4))
    println(A(5))
    println(A(6))
    println(A(7))
    println(A(8))
    println(A(9))
    println(A(10))
    println(A(11))
    println(A(12))
    println(A(13))
    println(A(14))
    println(A(15))


    /*
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

    println(A + B - B)
                 */


  }


}