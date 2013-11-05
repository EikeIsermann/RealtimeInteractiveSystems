package edges

import java.util.UUID
import nodes.Node
import scala.collection.immutable.HashMap

/**
 * Created by Christian Treffs
 * Date: 05.11.13 17:00
 */
abstract class Edge(_head: Node, _tail: Node) {
  val id = UUID.randomUUID()
  val head: Node = _head
  val tail: Node = _tail
  val attributes: HashMap = HashMap.empty()


}
