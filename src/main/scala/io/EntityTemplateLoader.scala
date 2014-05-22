package main.scala.io

import scala.xml.{Node, NodeSeq, XML, Elem}
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

  private val rootLabel = "entityTemplate"
  private val entityTemplates = mutable.HashMap.empty[Symbol,Entity]

  def get(name: Symbol): Entity = {
    if(!entityTemplates.contains(name)) {
      throw new IllegalArgumentException("no template with name '"+name.name+"' found")
    }
    entityTemplates(name)
  }

  def find(name: String): Entity = {
    val etK = entityTemplates.keys.filter(_.name.toLowerCase == name.toLowerCase)
    entityTemplates(etK.head)
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
        case hp: hasParts =>
          if(!hp.parts.forall(entityTemplates.contains)) {
            throw new IllegalArgumentException("a part of entity template '"+e+"' is missing")
          }
        case iPO: isPartOf =>
          if(!entityTemplates.contains(iPO.part)) {
            throw new IllegalArgumentException("parent entity '"+iPO.part+"' is missing in entity template '"+e+"'")
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
    val entity = Entity.create(name.name, template = true)


    // load mesh if there is one
    val mesh: NodeSeq = xml \ "mesh"
    mesh.foreach(m => {
      val src = (m \ "source").text
      Collada.load(name,src)
    })

    // parse components

    parseComponents(entity,xml)




    entityTemplates.put(name, entity)



  }
  def parseComponents(entity: Entity,xml: Node) {
    //TODO: add components
    //entity += Ammo.fromXML(xml)
    entity += CamControl.fromXML(xml)
    entity += Camera.fromXML(xml)
    entity += Collision.fromXML(xml)
    entity += Display.fromXML(xml)
    entity += DriveControl.fromXML(xml)
    entity += Gun.fromXML(xml)
    entity += GunControl.fromXML(xml)
    entity += hasParts.fromXML(xml)
    entity += Health.fromXML(xml)
    //entity += LifeTime.fromXML(xml)
    //entity += Light.fromXML(xml)
    entity += isPartOf.fromXML(xml)
    entity += Physics.fromXML(xml)
    entity += Placement.fromXML(xml)
    entity += Projectile.fromXML(xml)
    entity += Sound.fromXML(xml)
    entity += Text.fromXML(xml)
    entity += Vehicle.fromXML(xml)
  }
}
