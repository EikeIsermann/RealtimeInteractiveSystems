package main.scala.tools

import main.scala.systems.input.{Key, Input}

/**
 * Created by Christian Treffs
 * Date: 20.03.14 16:56
 */
object GameConsole {

  private var active = false
  private var first = true
  private val text: StringBuffer = new StringBuffer()
  val validKeys: Seq[Key.Value] = Key.numbers ++ Key.literals ++ Seq(Key.Period, Key.Comma, Key.Space)

  def isActive:Boolean = active

  def enable() {
    active = true
  }
  def disable() {
    active = false
    first = true
  }

  def toggleState() {
    if(active) disable()
    else enable()
  }

  def newLine() {
    val command = text.toString
    if(!command.isEmpty) {
      println()
      DC.log("Command:",command)
    }
    text.delete(0,text.length())
    print("\n>")
  }


  def write(character: String) {
    text.append(character)
    print(character)

  }

  def deleteLastCharacter() {
    if(text.length() > 0) {
      text.deleteCharAt(text.length()-1)
    }
  }


  def updateInput() {


  active = Input.isKeyToggled(Key.GameConsole)

    if(isActive) {
      if(first) {
        newLine()
      }

      Input.keyDownOnceDo(Key.Enter, _ => newLine())
      Input.keyDownOnceDo(Key.BackSpace, _ => deleteLastCharacter())
      validKeys.foreach(l => {
        Input.keyDownOnceDo(l,_ => write(Key.asChar(l).toString))
      })


    first = false
}

}

}
