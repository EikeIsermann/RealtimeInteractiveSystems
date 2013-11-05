package nodes

import java.util.UUID
import scala.collection.immutable.HashMap
import edges.Edge

/**
 * Created by Christian Treffs
 * Date: 05.11.13 17:00
 */
trait Node {
  val id = UUID.randomUUID()
  val childNodes = HashMap.empty[Node, Edge]

}
