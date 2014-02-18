package main.scala.math

/**
 * Created by Christian Treffs
 * Date: 23.01.14 20:35
 */
object Vertex {
  implicit def v(pos: Vec3f, col: Color) = new Vertex(pos, col)
}
case class Vertex(position: Vec3f, color: Color) {




}
