package main.scala.tools

import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 21.03.14 09:52
 */
object Identifier {
  final val identifier = mutable.HashMap.empty[String, Long]

  protected def getNextId(name: String): Long = {
    val n = name.toUpperCase
    var id: Long = 0
    identifier.get(n).collect{
      case i: Long => id = i+1
    }
    identifier.put(n, id)
    id
  }

  def create(name: String): Identifier = new Identifier(name.toUpperCase, getNextId(name))

}
sealed case class Identifier(name: String, id: Long) {
  def equals(i: Identifier): Boolean = this.==(i)
  def ==(e: Identifier): Boolean = e.id == this.id && e.name == this.name
  override def toString: String = name+":"+id
}
