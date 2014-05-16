package main.scala.io

import main.scala.engine.GameEngine
import scala.xml.XML

/**
 * Created by Christian Treffs
 * Date: 16.05.14 17:31
 */
object LevelLoader {



}

class Level {

  def save() {

  }
  def saveAs(name: String, fileName: String) {
    val xml = GameEngine.entities.values.map(_.toXML)
    val lvl = <level name={name}>{xml}</level>

    XML.save(GameEngine.levelsDir+"/"+fileName+".lvl",lvl)

  }
}
