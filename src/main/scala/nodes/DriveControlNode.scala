package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Physics, DriveControl, Vehicle}

/**
 * User: uni
 * Date: 20.05.14
 * Time: 12:29
 * This is a RIS Project class
 */
class DriveControlNode(veh: Vehicle, con: DriveControl, phy: Physics) extends Node {
  def this() = this(new Vehicle(), new DriveControl(), new Physics())

}
