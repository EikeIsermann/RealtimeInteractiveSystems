package main.scala.architecture

import scala.collection.mutable
import main.scala.tools.DC
import main.scala.nodes._
import main.scala.components._
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
        retVal.put(true, List(classOf[CamControl], classOf[Camera], classOf[Placement]))
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
        retVal.put(true, List(classOf[Physics],classOf[Placement]))
        retVal.put(false, List())
      }

      case physnode if physnode == classOf[PhysicsNode] => {
        retVal.put(true, List(classOf[Placement], classOf[Physics]))
        retVal.put(false, List())
      }

      case rennode if rennode == classOf[RenderNode] => {
        retVal.put(true, List(classOf[Placement], classOf[Display]))
        retVal.put(false, List())
      }

     case soundNode if soundNode == classOf[SoundNode] => {
        retVal.put(true, List(classOf[Sound], classOf[Placement], classOf[Physics]))
        retVal.put(false, List())
      }
      case posroot if posroot == classOf[PositionalRootNode] => {
        retVal.put(true, List(classOf[Placement], classOf[Children]))
        retVal.put(false, List(classOf[Parent]))
      }
      case guncontrol if guncontrol == classOf[GunControlNode] => {
        retVal.put(true, List(classOf[GunControl], classOf[Gun], classOf[Placement]))
        retVal.put(false, List())
      }
      case drivecontrol if drivecontrol == classOf[DriveControlNode] => {
        retVal.put(true, List(classOf[DriveControl], classOf[Physics], classOf[Vehicle], classOf[Placement]))
        retVal.put(false, List())
      }
      case gunnode if gunnode == classOf[GunNode] => {
        retVal.put(true, List(classOf[Gun], classOf[Placement]))
        retVal.put(false, List())
      }
      case gameConsoleNode if gameConsoleNode == classOf[GameConsoleNode] => {
        retVal.put(true, List(classOf[GameConsole]))
        retVal.put(false, List())
      }
      case textNode if textNode == classOf[TextNode] => {
        retVal.put(true, List(classOf[GameConsole],classOf[Placement]))
        retVal.put(false, List())
      }

      case _ => DC.warn("No valid definition for " + cl.getClass.getSimpleName)
    }
    retVal
  }


}