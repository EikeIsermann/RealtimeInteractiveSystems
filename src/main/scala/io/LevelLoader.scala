package main.scala.io

import main.scala.engine.GameEngine
import scala.xml.PrettyPrinter
import java.io.FileOutputStream
import java.nio.channels.Channels
import scala.collection.immutable.HashMap

/**
 * Created by Christian Treffs
 * Date: 16.05.14 17:31
 */
object LevelLoader {
  final val encoding = "UTF-8"
  private val levels = HashMap.empty[Symbol,Level]

  def get(name: Symbol): Level = {
    if(!levels.contains(name)) {
      throw new IllegalArgumentException("no level with name '"+name.name+"' found")
    }
    levels(name)
  }

  def load(fileName: String = null,extension: String = "lvl",pathDir: String = GameEngine.levelsDir) {

    if(fileName == null) {
      loadAllLevels()
    } else {
      loadOneLevel()
    }

  }

  private def loadAllLevels() = {}
  private def loadOneLevel() = {}

}

class Level(nameLvl: String) {
  if(nameLvl == null || nameLvl.isEmpty) {
    throw new IllegalArgumentException("Level name must not be empty!")
  }

  private val _name: String = nameLvl

  def name: String = _name

  def save() = saveAs(name)
  def saveAs(fileName: String) = {
    val xml = GameEngine.entities.values.map(_.toXML)
    val pp = new PrettyPrinter(80,2)
    val fos = new FileOutputStream(GameEngine.levelsDir+"/"+fileName+".lvl")
    val writer = Channels.newWriter(fos.getChannel, LevelLoader.encoding)
    val lvl = <level name={name}>{xml}</level>
    writer.write(pp.format(lvl))
    writer.close()
    fileName
  }
}
