package main.scala.io

import main.scala.engine.GameEngine
import scala.xml.{Elem, XML, PrettyPrinter}
import java.io.{File, FileOutputStream}
import java.nio.channels.Channels
import scala.collection._
import main.scala.tools.{Identifier, DC}
import main.scala.entities.Entity
import scala.collection.mutable.ListBuffer
import main.scala.components.{Parent, Children, hasParts, isPartOf}
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Created by Christian Treffs
 * Date: 16.05.14 17:31
 */
object LevelLoader {
  final val encoding = "UTF-8"
  private val rootLabel = "level"
  private val levels = mutable.HashMap.empty[Symbol,Level]

  def get(name: Symbol): Level = {
    if(!levels.contains(name)) {
      throw new IllegalArgumentException("no level with name '"+name.name+"' found")
    }
    levels(name)
  }

  def load(fileName: String = null,extension: String = "lvl",pathToDir: String = GameEngine.levelsDir) {
    if(fileName == null)
      loadAllLevels(FileIO.loadAll(extension, pathToDir))
     else
      loadOneLevel(FileIO.load(pathToDir+"/"+fileName+"."+extension))
  }

  private def loadAllLevels(files: Seq[File]) = files.map(loadOneLevel)
  private def loadOneLevel(file: File) = {
    val xml = XML.loadFile(file)

    if(xml.label != rootLabel) {
      throw new IllegalArgumentException("not a valid level '"+xml.label+"'")
    }

    val nameStr: String = (xml \ "@name").text
    if(nameStr == null || nameStr.isEmpty) {
      throw new IllegalArgumentException("not a valid level - name is corrupt '"+nameStr+"'")
    }
    val lvl = new Level(nameStr)
    lvl.setXML(xml)
    DC.log("Level "+nameStr,"loaded",3)
    levels += Symbol(lvl.name) -> lvl
  }

}

class Level(nameLvl: String) {


  if(nameLvl == null || nameLvl.isEmpty) {
    throw new IllegalArgumentException("Level name must not be empty!")
  }

  private val _name: String = nameLvl
  private var _xml: Elem = null

  def name: String = _name

  def save() = saveAs(name)
  def saveAs(fileName: String) = {
    DC.logT('savingLevel,"Saving Level",name,3)
    val xml = GameEngine.entities.values.map(_.toXML)
    val pp = new PrettyPrinter(80,2)
    val fos = new FileOutputStream(GameEngine.levelsDir+"/"+fileName+".lvl")
    val writer = Channels.newWriter(fos.getChannel, LevelLoader.encoding)
    val now = Calendar.getInstance().getTime
    val sdf = new SimpleDateFormat()
    val saveTime = sdf.format(now)
    val lvl = <level name={name} saveTime={saveTime.toString}>{xml}</level>
    writer.write(pp.format(lvl))
    writer.close()
    DC.logT('savingLevel,"Level saved",name,3)
    fileName
  }

  def initialize() {

  /*
    GameEngine.pause(Seq())


    GameEngine.entities.values.foreach(_.destroy())
    GameEngine.entities.clear()
    Identifier.identifier.clear()
                                   */


    if(_xml == null) {
      throw new IllegalArgumentException("level is empty can't load anything")
    }
    val xml = _xml
    val entities = xml \ "entity"
    var entityList: ListBuffer[Entity] = new ListBuffer[Entity]()
    entities.foreach(entityXML => {
      val eId = (entityXML \ "@identifier").text

      val eIdSplit = eId.split(Identifier.separator).toList
      val name = eIdSplit(0).toString
      val id = eIdSplit(1).toLong
      val ent = new Entity(Identifier.create(name,id))
      //parsing and registering the components
      entityXML.foreach(a => EntityTemplateLoader.parseComponents(ent,a))
      if(ent.has(classOf[hasParts])) entityList.+=(ent)
      }
    )
    for(e <- entityList){
        val children = new Children()
        children.owner = e.identifier
        // for each part that is child of this entity
        e.getComponent(classOf[hasParts]).parts.foreach(part => {
          // create child
          var childEntity = GameEngine.entities.apply(part.name)
          // add this entity as parent
          val p1 = new Parent(e)
          p1.owner = e.identifier
          childEntity += p1
          DC.log("Child entity added to parent entity",(childEntity,e),1)
          // add each child to this entity as a child
          children += childEntity
        })

        // add all children
        e += children
      //do nothing - this can only be checked backwards
      // by doing nothing it is prevented, that the isPartOf component is not added to the real entity

    }

    //GameEngine.resume()



  }


  def setXML(xml: Elem) = {
    _xml = xml
  }
}
