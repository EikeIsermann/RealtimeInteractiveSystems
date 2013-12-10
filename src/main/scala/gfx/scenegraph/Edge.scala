package main.scala.gfx.scenegraph

import java.util.UUID
import scala.collection.mutable
import main.scala.gfx.scenegraph.Node

/**
 * Created by Christian Treffs
 * Date: 05.11.13 17:00
 */
abstract case class Edge(head: Node, tail: Node) {
  val id = UUID.randomUUID()
  val attributes = mutable.HashMap.empty[String, String]

  head.connectChild(this, tail)
  tail.connectParent(this, head)

  def addAttribute(name: String, attr: String) = attributes.put(name, attr)

}
