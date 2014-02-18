package main.scala.math

/**
 * Created by Christian Treffs
 * Date: 23.01.14 20:36
 */

object Color {
  implicit def apply(r: Float, g: Float, b: Float) = new Color(Vec3f(r, g, b))
  implicit def apply(vec: Vec3f) = new Color(vec)

}
class Color(vector: Vec3f) extends ogl.vecmathimp.ColorImp(vector) {



}
