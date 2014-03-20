package main.scala.tools

import main.scala.systems.input.{Key, Input}
import scala.collection.mutable

/**
 * Created by Christian Treffs
 * Date: 20.03.14 16:56
 */
object GameConsole {

  private var active = false
  private var first = true
  private var lastT: Float = 0
  private val minTimeBetweenInputs: Long = 50
  private var delta: Float = 0
  private val text: StringBuffer = new StringBuffer()
  private val charMap = mutable.HashMap.empty[String, Long]

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

  private def canWrite(deltaT: Float): Boolean = {
    if(lastT < minTimeBetweenInputs) {
      lastT = lastT + deltaT
      false
    } else {
      lastT = 0
      true
    }


  }

  def write(character: String) {
    val lastTime = charMap.get(character)
    lastTime.collect {
      case t: Long => {
        val deltaT: Long = System.currentTimeMillis()-t
        if(deltaT > minTimeBetweenInputs) {
          text.append(character)
          print(character)
        }
      }
      case _ => {
        text.append(character)
        print(character)
      }
    }
    charMap.put(character, System.currentTimeMillis())
  }

  def deleteLastCharacter() {
    if(text.length() > 0) {
      text.deleteCharAt(text.length()-1)
    }
  }


  def updateInput(deltaT: Float) {

    delta = deltaT

    if(Input.keyToggled(Key.GameConsole)) {
      if(first) {
        newLine()
      }


        Input.keyDown(Key.Enter, _ => newLine())
        Input.keyDown(Key._Q, _ => write("q"))
        Input.keyDown(Key._W, _ => write("w"))
        Input.keyDown(Key._E, _ => write("e"))
        Input.keyDown(Key.BackSpace, _ => deleteLastCharacter())








      first = false
    }

  }

}
