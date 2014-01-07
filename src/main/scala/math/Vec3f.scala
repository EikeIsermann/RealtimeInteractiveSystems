package main.scala.math



/**
 * Created by Christian Treffs
 * Date: 07.01.14 11:48
 */

object Vec3f {
  implicit def Vec3f(vec: ogl.vecmath.Vector) = new Vec3f(vec.x, vec.y, vec.z)
}

final case class Vec3f(x1: Float = 0, y1: Float = 0, z1: Float = 0) extends ogl.vecmathimp.VectorImp(x1: Float, y1: Float, z1: Float) {



  implicit def *(s: Float) = mult(s)
  implicit def *(v: Vec3f) = mult(v)
  implicit def +(v: Vec3f) = add(v)
  implicit def -(v: Vec3f) = sub(v)

  implicit def toArray: Array[Float] = asArray()
  implicit def toSeq: Seq[Float] = toArray
  implicit def toTuple3: (Float, Float, Float) = Tuple3(x, y, z)



}
