package main.scala.io

import scala.xml.{NodeSeq, XML, Elem}
import java.io.File
import main.scala.components._
import main.scala.entities.Entity
import main.scala.tools.DC
import scala.collection.mutable


/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:31
 */
object EntityTemplateLoader {

  def main(args: Array[String]) {
    EntityTemplateLoader.load("src/main/resources/entities/")


    val s = System.currentTimeMillis()
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    Entity.newInstanceOf('Tank)
    val q = Entity.newInstanceOf('Tank)
    val e = System.currentTimeMillis()
    println(e-s)

    println(q.toXML)
  }

  private val rootLabel = "entityTemplate"
  private val entityTemplates = mutable.HashMap.empty[Symbol,Entity]

  def get(name: Symbol): Entity = {
    if(!entityTemplates.contains(name)) {
      throw new IllegalArgumentException("no template with name '"+name.name+"' found")
    }
    entityTemplates(name)
  }

  def load(pathToDir: String, extension: String = "ntt") {
    val files = FileIO.loadAll(extension, pathToDir)
    createEntityTemplates(files)
    DC.log("Entity templates created", entityTemplates.size, 2)
    crosscheckDependencies()
    DC.log("Entity template dependencies", "ok", 2)
  }

  private def createEntityTemplates(files: Seq[File])= files.map(f => parse(f))


  private def crosscheckDependencies() {

    entityTemplates.values.foreach(e => {
      e.components.foreach {
        case hp: hasParts => {
          if(!hp.parts.forall(entityTemplates.contains)) {
            throw new IllegalArgumentException("a part of entity template '"+e+"' is missing")
          }
        }
        case iPO: isPartOf => {
          if(!entityTemplates.contains(iPO.part)) {
            throw new IllegalArgumentException("parent entity '"+iPO.part+"' is missing in entity template '"+e+"'")
          }
        }
        case _ =>
      }

    })

  }

  private def parse(file: File) = {
    val xml: Elem = XML.loadFile(file)

    if(xml.label != rootLabel) {
      throw new IllegalArgumentException("not a valid entity template '"+xml.label+"'")
    }

    val nameStr: String = (xml \ "@name").text
    if(nameStr == null || nameStr.isEmpty) {
      throw new IllegalArgumentException("not a valid entity template - name is corrupt '"+nameStr+"'")
    }

    val name: Symbol = Symbol(nameStr)

    val entity = Entity.create(name.name)


    val mesh: NodeSeq = xml \ "mesh"

    mesh.foreach(m => {
      val src = (m \ "source").text
      Collada.load(name,src)
    })



    entity += hasParts.fromXML(xml)
    entity += isPartOf.fromXML(xml)
    entity += Display.fromXML(xml)
    entity += Motion.fromXML(xml)
    entity += Placement.fromXML(xml)

    //TODO: add componentes


    entityTemplates.put(name, entity)



  }
}
