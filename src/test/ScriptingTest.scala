package test

import java.io.File

/**
 * Created by Christian Treffs
 * Date: 10.12.13 14:37
 */
object ScriptingTest {
  def main(args: Array[String]): Unit = {
    val Array(directory,script) = args.map(new File(_).getAbsolutePath)
    println("Executing '%s' in directory '%s'".format(script, directory))
  }
}
