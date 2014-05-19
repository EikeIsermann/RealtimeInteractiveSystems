package main.scala.nodes

import main.scala.components.{Placement, Gun, GunControl}
import main.scala.architecture.Node

/**
 * User: uni
 * Date: 19.05.14
 * Time: 21:30
 * This is a RIS Project class
 */
class GunControlNode(control: GunControl, gun: Gun, placement: Placement) extends Node(control, gun, placement) {

def this() = this(new GunControl(), new Gun(), new Placement())


}
