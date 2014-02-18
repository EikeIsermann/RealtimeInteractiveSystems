package main.scala.world.entities

import main.scala.math.{Color, Vec3f, Vertex}

/**
 * Created by Christian Treffs
 * Date: 23.01.14 20:34
 */
trait Shape {

  /**
   * returns all vertices of this shape
   * @return array with vertices
   */
  def vertices: Array[Vertex]

  /**
   * returns the color at this position
   * @param pos the position
   * @return the color
   */
  def colorAt(pos: Vec3f): Color = vertices.filter(_.position == pos).head.color


}
