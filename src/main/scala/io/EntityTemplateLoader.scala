package main.scala.io

import scala.xml.{NodeSeq, XML, Elem}
import java.io.File
import main.scala.math.Vec3f
import scala.collection.mutable.ListBuffer
import main.scala.components._
import main.scala.entities.Entity
import main.scala.tools.DC
import scala.collection.mutable
import main.scala.components.ParentEntity


/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:31
 */
object EntityTemplateLoader {

  def main(args: Array[String]) {
    EntityTemplateLoader.load("src/main/resources/entities/")
  }

  private val rootLabel = "entityTemplate"
  private val entityTemplates = mutable.HashMap.empty[Symbol,Entity]

  def get(name: Symbol): Entity = {
    if(!entityTemplates.contains(name)) {
      throw new IllegalArgumentException("no template with name '"+name.name+"' found")
    }
    entityTemplates(name)
  }

  def load(pathToDir: String, extension: String = "ntt") = {
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


    entity += hasParts.fromXML(xml)
    entity += isPartOf.fromXML(xml)
    entity += Display.fromXML(xml)
    entity += Motion.fromXML(xml)
    entity += Placement.fromXML(xml)

    //TODO: add componentes

    println(name, entity)
    entity.components.map(println)
     /*
    val name: String = (xml \ "name").text
    val graphicsComponent = xml \ "components" \ "graphics"
    val physicsComponent = xml \ "components" \ "physics"
    //val aiComponent = xml \ "components" \ "ai"
    //val logicComponent = xml \ "components" \ "logic"

    val ret = ListBuffer.empty[Entity]

    val entity = Entity.create(name)



    // READ GFX
    graphicsComponent foreach(
      gc => {
        val uses: Seq[Node] =gc.nonEmptyChildren.filter(_.label == "use").map(n => {(xml \ n.text).head})
        val has: Seq[Node] = gc.nonEmptyChildren.filter(_.label != "use")
        val attrs = NodeSeq.fromSeq(uses ++ has)
        val transformation = (attrs \\ "transformation").head

        // get position
        val positionComponent = Placement.fromXML(transformation)

        // create a parent entity as reference for child entites
        val parentComp: ParentEntity = ParentEntity(entity)

        val shaderId:Symbol = Symbol((attrs \\ "shader").head.text)


        // aggregate child entities
        val childEntities = parseMeshes(Symbol(name), attrs \\ "meshes", parentComp, shaderId)
        // add them to the return seq
        ret ++= childEntities

        // add the children to the current entity
        val childrenComponent: ChildEntities = ChildEntities(childEntities)

        //assemble entity
        entity.add(positionComponent) //position component
        entity.add(childrenComponent)

        //TODO: Family

      })


    //READ Physics
    physicsComponent foreach(
      pc => {

      }
      )


    ret += entity

    ret.toSeq
    */

    entityTemplates.put(name, entity)

  }


  def parseMeshes(identifier: Symbol, meshes: NodeSeq, parentComp: ParentEntity, shaderId: Symbol): Seq[Entity] = {

    val sourceStr = (meshes \ "source").text

    // load collada file to generate all meshes
    Collada.load(identifier, sourceStr)

    val seqChildEntities = ListBuffer.empty[Entity]
    val ms = meshes \\ "mesh"
    ms.foreach(
      mesh => {
        val meshId = Symbol((mesh \ "@id").text)

        val childEntity   = Entity.create(meshId.name)
        val childPos      = Placement.fromXML(mesh)
        val childDisplay  = new Display(meshId, shaderId)

        //add position, display and parentEntity to child entity
        childEntity += childPos
        childEntity += childDisplay
        childEntity += parentComp

        //TODO: Families

        //add the children to the sequence
        seqChildEntities += childEntity

    })



    seqChildEntities.toSeq
  }


  def parseTransformation(trafo: NodeSeq): (Vec3f, Vec3f, Vec3f) = {



    val pos = trafo \ "position"
    val position = Vec3f((pos \ "@x").text.toFloat, (pos \ "@y").text.toFloat, (pos \ "@z").text.toFloat)

    val rot = trafo \ "rotation"
    val rotation = Vec3f((rot \ "@angleX").text.toFloat, (rot \ "@angleY").text.toFloat, (rot \ "@angleZ").text.toFloat)

    val sc = trafo \ "scale"
    val scale = Vec3f((sc \ "@x").text.toFloat, (sc \ "@y").text.toFloat, (sc \ "@z").text.toFloat)

    (position,rotation,scale)
  }

}
