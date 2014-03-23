package main.scala.math



/**
 * Created by Christian Treffs
 * Date: 07.01.14 11:48
 */

object Vec3f {
  implicit def Vec3f(vec: ogl.vecmath.Vector) = new Vec3f(vec.x, vec.y, vec.z)
  implicit def Vec3f(equalXYZ: Float) = new Vec3f(equalXYZ, equalXYZ, equalXYZ)
}

final case class Vec3f(x1: Float = 0, y1: Float = 0, z1: Float = 0) extends ogl.vecmathimp.VectorImp(x1: Float, y1: Float, z1: Float) {

  implicit def *(s: Float) = mult(s)
  implicit def *(v: Vec3f) = mult(v)
  implicit def +(v: Vec3f) = add(v)
  implicit def -(v: Vec3f) = sub(v)

  def mapX(min: Float, max: Float, shift: Float = -1.0f): Float = (2.0f * x()) / (max-min) + shift
  def mapY(min: Float, max: Float, shift: Float = -1.0f): Float = (2.0f * y()) / (max-min) + shift
  def mapZ(min: Float, max: Float, shift: Float = -1.0f): Float = (2.0f * z()) / (max-min) + shift
  def mapVec(min: Float, max: Float, shift: Float = -1.0f): Vec3f = {
    Vec3f(
      mapX(min, max, shift),
      mapY(min, max, shift),
      mapZ(min, max, shift)
    )
  }

  implicit def toArray: Array[Float] = asArray()
  implicit def toSeq: Seq[Float] = toArray
  implicit def toTuple3: (Float, Float, Float) = Tuple3(x, y, z)

  def inline: String = "["+x+" "+y+" "+z+"]"

  def magnitude: Double = math.sqrt((x() * x()) + (y() * y()) + (z() * z()))

  def normalized: Vec3f = {
    val a: Float = magnitude.toFloat
    Vec3f(
      x/a,
      y/a,
      z/a
    )
  }

  override def toString: String = {
    "Vec3f:\n"+
    "[ "+x1+ " ]\n" +
    "[ "+y1+ " ]\n"  +
    "[ "+z1+ " ]\n"
  }


}
