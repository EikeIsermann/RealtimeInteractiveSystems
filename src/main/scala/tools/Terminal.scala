package main.scala.tools

import scala.tools.jline.console.ConsoleReader
import scala.collection.mutable.ListBuffer

/**
 * Created by Christian Treffs
 * Date: 12.12.13 15:47
 */


trait TerminalReader {
  private val _keywords = ListBuffer[String]()
  def registerKeyword(word: String) = {if(!keywords.contains(word)) _keywords += word}
  def keywords = _keywords.toSeq
  def readLine(line: String): Unit
  def keyWord: String
  def help(): String
  def isRelevant(line: String): Boolean = {
    if(line.trim.toLowerCase.equals(keyWord.toLowerCase)) true
    else false
  }
}

object Terminal {

  private var c: Terminal = null

  val separator = " "
  val exitCommands = Array("exit", "quit", "kill")

  def to(readLineTo: (String => Unit)*) {
    c match {
      case null => {
        c = new Terminal(readLineTo)
        c.start()
      }
      case _ => c.addOutputs(readLineTo)
    }
  }

  def end() {
    if(c != null) c.halt()
  }


}

private class Terminal(readLineToFunc: Seq[(String) => Unit]) extends Thread {

  private val cr  = new ConsoleReader()
  private var outputs: Seq[(String) => Unit] = readLineToFunc

  override def run() {
    println("[console] running")
    while (!Thread.interrupted()) {
      try {
       readLineTo()
      } catch {
        case i:InterruptedException => Thread.currentThread().interrupt()
        case e:Exception => e.printStackTrace()
      }
    }
    println("[console] finished")
  }

  protected def addOutputs(newOutputs: Seq[(String) => Unit]) {
     outputs = outputs ++ newOutputs
  }

  private def readLine(): String = {
    val line = cr.readLine()
    if(Terminal.exitCommands.contains(line) || line == null || !line.isInstanceOf[String]) {
     halt()
     return "exit" //TODO good exit code?
    }
    else if(!line.isEmpty) {
      val a = line.split(Terminal.separator)
      /*if(a.length > 1 && a(1).toLowerCase().trim == "help") {
        return //TODO
      }*/
    }

    line


  }

  private def readLineTo() {
    // read the console line
    val result = readLine()
    // apply result to all outputs
    outputs.foreach(_.apply(result))
  }

  protected def halt() {
    println("[console] shutting down")
    this.interrupt()
  }
}
