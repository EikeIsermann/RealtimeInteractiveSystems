package main.scala.architecture

import scala.collection.mutable
import main.scala.tools.DC
import scala.collection.mutable.HashMap
import main.scala.nodes._
import main.scala.components._
import main.scala.components.Camera
import main.scala.components.CamControl
import main.scala.components.CamControl
import main.scala.components.Camera
import main.scala.nodes.CameraNode
import main.scala.nodes.CollisionNode

/**
 * Created by Christian Treffs
 * Date: 20.03.14 20:51
 *
 * A Node is a combination of one or more Components.
 */
abstract class Node(args: Component*) {
  var components:  mutable.HashMap[Class[_ <: Component], Component] = new  mutable.HashMap[Class[_ <: Component], Component]

  for(comp <- args){
    components.put(comp.getClass, comp)
  }

  def -> [T <: Component](c: Class[T]): T = {
    components.apply(c).asInstanceOf[T]
  }


  override def toString: String = this.getClass.getSimpleName
}

object Node {

  def getDefinition(cl : Class[_ <: Node]): mutable.HashMap[Boolean, List[Class[_ <: Component]]] = {
    val retVal = new mutable.HashMap[Boolean, List[Class[_ <: Component]]]

    cl match {
      case camcontrol if camcontrol == classOf[CamControlNode] => {
        retVal.put(true, List(classOf[CamControl], classOf[Motion]))
        retVal.put(false, List())
      }
      case camnode if camnode == classOf[CameraNode] => {
        retVal.put(true, List(classOf[Camera], classOf[Placement]))
        retVal.put(false, List())
      }
      case colnode if colnode == classOf[CollisionNode] => {
        retVal.put(true, List(classOf[Collision], classOf[Placement]))
        retVal.put(false, List())

      }
      case movnode if movnode == classOf[MovementNode] => {
        retVal.put(true, List(classOf[Motion],classOf[Placement]))
        retVal.put(false, List())
      }

      case physnode if physnode == classOf[PhysicsNode] => {
        retVal.put(true, List(classOf[Physics], classOf[Placement], classOf[Motion]))
        retVal.put(false, List())
      }

      case rennode if rennode == classOf[RenderNode] => {
        retVal.put(true, List(classOf[Placement], classOf[Display]))
        retVal.put(false, List())
      }

      case soundNode if soundNode == classOf[SoundNode] => {
        retVal.put(true, List(classOf[Sound], classOf[Placement], classOf[Motion]))
        retVal.put(false, List())
      }
      case _ =>
    }
    retVal
  }


}