package main.scala.components

import main.scala.architecture.Component
import scala.xml.Node
import main.scala.tools.Identifier
import main.scala.math.{Mat3f, Vec3f}

/**
 * Created by Christian Treffs
 * Date: 01.04.14 16:23
 */
class Physics(mass1: Float = 1f, linearDampening1: Float = 0.99f, inertia1: Mat3f = Mat3f(), angularDampening1: Float = 0.8f, gravity1: Vec3f = Vec3f(0,10f,0)) extends Component{




  override def newInstance(identifier: Identifier): Component = ???

  override def toXML: Node = ???
}
