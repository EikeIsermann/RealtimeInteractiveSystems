package main.scala.systems.gameplay.states

import main.scala.systems.input.{Key, Input}
import main.scala.engine.GameEngine
import main.scala.architecture.System
import main.scala.systems.gfx.{TextRenderingSystem, RenderingSystem}
import main.scala.tools.DC
import main.scala.components.{Text, Placement}
import main.scala.entities.Entity
import main.scala.io.EntityTemplateLoader
import main.scala.math.Vec3f

/**
 * Created by Christian Treffs
 * Date: 22.05.14 11:46
 */
class InGameConsole extends GameState {

  private val validKeys: Seq[Key.Value] = Key.numbers ++ Key.literals ++ Seq(Key.Period, Key.Comma, Key.Space)
  private val stringBuffer: StringBuilder = new StringBuilder()
  private var f: Boolean = true
  private var textEntity: Entity = null

  override def execute()(implicit owner: System): GameState = {
    // pause the systems
    GameEngine.pause(Seq(owner.getClass, classOf[RenderingSystem], classOf[TextRenderingSystem]))

    // print console activated the first time
    first()

    processKeys()

    display()

    // return other states
    Input.keyDownOnceDo(Key.GameConsole, _ => {last();return new Playing}) // goto Playing
    this // go to console again
  }

  private def first() {
    if(f) {
      textEntity = Entity.create("GameConsoleText")
      textEntity.add(new Text())
      textEntity.add(new Placement(Vec3f(-0.1f,0.3f,-1f)))
      DC.log("Console","activated",3)
      print("> ")
      f = false
    }
  }

  private def display() {
     textEntity.getIfPresent(classOf[Text]).map(_.text = stringBuffer.toString())


  }


  private def processKeys() {

    Input.keyDownOnceDo(Key.Enter, _ => {newLine(); processInput()})

    validKeys.foreach(l => {
      Input.keyDownOnceDo(l,_ => add(Key.asChar(l).toString))
    })
  }


  private def processInput() {
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

  private def newLine() {
    println()
    print("> ")
  }


  private def last() {
    println()
    DC.log("Console","deactivated",3)
    textEntity.destroy()
    f = true
  }

  private def add(s: String): Unit = {
    stringBuffer.append(s)
    print(s)
  }

  def clear() = stringBuffer.clear()

}
