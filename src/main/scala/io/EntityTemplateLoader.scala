package main.scala.io

import scala.xml.{NodeSeq, XML, Elem}
import java.io.File
import main.scala.components._
import main.scala.entities.EntityManager
import main.scala.tools.DC
import scala.collection.mutable


/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:31
 */
object EntityTemplateLoader {

  private val rootLabel = "entityTemplate"
  private val entityTemplates = mutable.HashMap.empty[Symbol,EntityManager]

  def get(name: Symbol): EntityManager = {
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

    // create the entity template
    val entity = EntityManager.create(name.name, template = true)


    // load mesh if there is one
    val mesh: NodeSeq = xml \ "mesh"
    mesh.foreach(m => {
      val src = (m \ "source").text
      Collada.load(name,src)
    })

    // parse components
    entity += hasParts.fromXML(xml)
    entity += isPartOf.fromXML(xml)
    entity += Display.fromXML(xml)
    entity += Motion.fromXML(xml)
    entity += Placement.fromXML(xml)

    //TODO: add components


    entityTemplates.put(name, entity)



  }
}
