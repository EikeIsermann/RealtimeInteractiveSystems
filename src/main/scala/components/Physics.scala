package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.tools.Identifier
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:23
 */
class Physics(mass1: Float, inertia1: Float, gForce1: Vec3f = Vec3f(0,0,9.81f)) extends Component{

  private var _mass: Float = mass1
  private var _inertia: Float = inertia1
  private var _gForce: Vec3f = gForce1

  def mass: Float = _mass
  def inertia: Float = _inertia
  def gForce: Vec3f = _gForce

  override def newInstance(identifier: Identifier): Component = ???

  override def toXML: Node = ???
}
