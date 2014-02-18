package main.scala.world.entities

import main.scala.input.SimulationContext
import main.scala.math.{Vec3f, Vertex, Color}
import main.scala.tools.DC


/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:47
 */
class Cube(cubeName: String, initialPosition: Vec3f = Vec3f(0, 0, 0), dimension: Vec3f = Vec3f(0.5f), lifeTime: Long = -1l) extends Entity with Shape {

  def width = dimension.x
  def height = dimension.y
  def depth = dimension.z

  private val w2 = width/2f
  private val h2 = height/2f
  private val d2 = depth/2f


  private val p: Array[Vec3f] = Array(
    Vec3f(-w2, -h2, -d2),
    Vec3f(w2, -h2, -d2),
    Vec3f(w2, h2, -d2),
    Vec3f(-w2, h2, -d2),
    Vec3f(w2, -h2, d2),
    Vec3f(-w2, -h2, d2),
    Vec3f(-w2, h2, d2),
    Vec3f(w2, h2, d2)
  )

  private val c: Array[Color] = Array(
    Color(0, 0, 0),
    Color(1, 0, 0),
    Color(1, 1, 0),
    Color(0, 1, 0),
    Color(1, 0, 1),
    Color(0, 0, 1),
    Color(0, 1, 1),
    Color(1, 1, 1)
  )

  private def v(pos: Vec3f, col: Color) = new Vertex(pos, col)

  /**
   * returns all vertices of this shape
   * @return array with vertices
   */
  def vertices: Array[Vertex] = Array(
    // front
    v(p(0), c(0)), v(p(1), c(1)), v(p(2), c(2)), v(p(3), c(3)),
    // back
    v(p(4), c(4)), v(p(5), c(5)), v(p(6), c(6)), v(p(7), c(7)),
    // right
    v(p(1), c(1)), v(p(4), c(4)), v(p(7), c(7)), v(p(2), c(2)),
    // top
    v(p(3), c(3)), v(p(2), c(2)), v(p(7), c(7)), v(p(6), c(6)),
    // left
    v(p(5), c(5)), v(p(0), c(0)), v(p(3), c(3)), v(p(6), c(6)),
    // bottom
    v(p(5), c(5)), v(p(4), c(4)), v(p(1), c(1)), v(p(0), c(0))
  )

  /**
   * get the entity's name
   * @return the name
   */
  def name: String = cubeName

  /**
   * get the lifetime of the entity
   * lifetime <= 0 equals infinite lifetime
   * @return lifetime in milliseconds
   */
  def lifetime: Long = lifeTime

  /**
   * creation and initialisation process
   */
  def init(context: SimulationContext): Boolean = {
    DC.log("Cube initialized", this)
    true
  }

  /**
   * physics simulation process
   */
  def simulate(context: SimulationContext): Boolean = {
    true
  }

  /**
   * rendering process
   */
  def render(context: SimulationContext): Boolean = {
    true
  }

  /**
   * destruction process
   */
  def destroy(): Boolean = ???
}
