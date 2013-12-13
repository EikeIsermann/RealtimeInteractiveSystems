package main.scala.tools

import scala.tools.jline.console.ConsoleReader

/**
 * Created by Christian Treffs
 * Date: 12.12.13 15:47
 */
object Terminal {
  private var c: Terminal = null

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
        case e:Exception => Thread.currentThread().interrupt()
      }
    }
    println("[console] finished")
  }

  protected def addOutputs(newOutputs: Seq[(String) => Unit]) {
     outputs = outputs ++ newOutputs
  }

  private def readLine(): String = {
    val line = cr.readLine()
    if(line == null || !line.isInstanceOf[String]) {
      throw new IllegalArgumentException("can not read input '"+line+"'")
    }
    //shutdown if exitCommand is found and return defined exit code
    if(Terminal.exitCommands contains line) {
     halt()
     return "exit" //TODO good exit code?
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
