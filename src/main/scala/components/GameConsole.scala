package main.scala.components

import main.scala.architecture.Component
import main.scala.tools.Identifier
import scala.xml.Node
import main.scala.systems.input.Key
import main.scala.entities.Entity
import main.scala.math.Vec3f
import main.scala.io.EntityTemplateLoader

/**
 * Created by Christian Treffs
 * Date: 21.05.14 22:26
 */
class GameConsole() extends Component {



  val validKeys: Seq[Key.Value] = Key.numbers ++ Key.literals ++ Seq(Key.Period, Key.Comma, Key.Space)

  val stringBuffer: StringBuilder = new StringBuilder()

  def add(s: String): Unit = {
    stringBuffer.append(s)
    print(s)
  }
  def add(c: Char): Unit = add(c.toString)

  def clear() = stringBuffer.clear()

  def processInput(): Unit = {
    val str: String = stringBuffer.toString().trim()
    clear()

    val s = str.split(" ")

    if(s.length > 0) {
      if(s.length > 1 && s(0) == "new") {
        val templateName = s(1)
        val ent: Entity = EntityTemplateLoader.find(templateName).newInstance()
        if(s.length > 2 && s(2) == "at") {
          val v = s(3).split(",")
          ent.getIfPresent(classOf[Placement]).map(_.position = Vec3f(v(0).toFloat,v(1).toFloat,v(2).toFloat))
        }

      }
    }

  }

  override def newInstance(identifier: Identifier): Component = new GameConsole()
  override def toXML: Node = ???
}
