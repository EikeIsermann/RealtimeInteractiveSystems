package main.scala.io

import main.scala.engine.GameEngine
import scala.xml.{PrettyPrinter, XML}
import java.io.FileOutputStream
import java.nio.channels.Channels

/**
 * Created by Christian Treffs
 * Date: 16.05.14 17:31
 */
object LevelLoader {



}

class Level {

  val encoding = "UTF-8"

  def save() {

  }
  def saveAs(name: String, fileName: String) {
    val xml = GameEngine.entities.values.map(_.toXML)
    val pp = new PrettyPrinter(80,2)
    val fos = new FileOutputStream(GameEngine.levelsDir+"/"+fileName+".lvl")
    val writer = Channels.newWriter(fos.getChannel, encoding)
    val lvl = <level name={name}>{xml}</level>
    writer.write(pp.format(lvl))
    writer.close()
  }
}
