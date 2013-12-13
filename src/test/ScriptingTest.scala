package test

import java.io.File
import scala.tools.jline.console.ConsoleReader
import java.util.concurrent.ThreadPoolExecutor
import main.scala.tools.Terminal

/**
 * Created by Christian Treffs
 * Date: 10.12.13 14:37
 */
object ScriptingTest {



  def main(args: Array[String]): Unit = {
  /*  val Array(directory,script) = args.map(new File(_).getAbsolutePath)
    println("Executing '%s' in directory '%s'".format(script, directory))*/



    def func1(d: Any) {

      println("ausgabe 1: "+d)
    }

    var h = ""
    def history(d: String) {
       h = h ++ " "+d
      println("history:"+ h)
    }

    Terminal.to(func1(_), history(_))



  }

}
