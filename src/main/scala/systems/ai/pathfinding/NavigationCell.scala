package main.scala.systems.ai.pathfinding

import main.scala.math.Vec3f

/**
 * User: uni
 * Date: 22.05.14
 * Time: 21:34
 * This is a RIS Project class
 */
object NavigationCell {
  def apply(p1: Vec3f, p2: Vec3f, p3: Vec3f): NavigationCell = new NavigationCell(p1,p2,p3)
  def apply(cellPoints: Array[Vec3f]): NavigationCell = new NavigationCell(cellPoints)

}

class NavigationCell(cellPoints: Array[Vec3f]){

  def this(p1: Vec3f, p2: Vec3f, p3: Vec3f) = this(Array(p1,p2,p3))

  var points = cellPoints

  def center(p1: Vec3f, p2: Vec3f): Vec3f = 0.5f*(p1+p2)

}
