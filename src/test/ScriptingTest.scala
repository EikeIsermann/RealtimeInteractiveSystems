package test

import java.io.File
import scala.tools.jline.console.ConsoleReader
import java.util.concurrent.ThreadPoolExecutor
import main.scala.tools.{TerminalReader, Terminal}

/**
 * Created by Christian Treffs
 * Date: 10.12.13 14:37
 */
object ScriptingTest {



  def main(args: Array[String]): Unit = {

    val fr = new FileReader()


    Terminal.to(fr.readLine)

  }

}


class FileReader() extends TerminalReader {

  registerKeyword(keyWord)

  def keyWord = "readFile"



  def readLine(line: String): Unit = {

    if(isRelevant(line)) {
      val a = line.split(Terminal.separator)
      println(keywords)

      if(a != null && !a.isEmpty) {
/*        val f = new File(a)
        if(f.isFile && f.canRead && f.exists()) {
          println("File: '"+f+"'")
        } else {
          println("pronlm")
        }*/
      }

    }


  }

  def help(): String = "This is a file reader. Type '"+keyWord+"' and the path to the file and hit enter."
}
