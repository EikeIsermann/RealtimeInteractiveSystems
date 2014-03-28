package main.scala.io

import scala.xml.{Node, NodeSeq, XML, Elem}
import java.io.File
import main.scala.math.Vec3f
import scala.collection.mutable.ListBuffer
import main.scala.components.{ChildEntities, ParentEntity, Display, Position}
import main.scala.entities.Entity
import main.scala.tools.DC


/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:31
 */
object EntityDescLoader {


  def load(pathToDir: String, extension: String = "xml"): Seq[Entity] = {
    val files = FileIO.loadAll(extension, pathToDir)
    val entities = createEntities(files)
    DC.log("Entities created", entities.length, 2)
    entities
  }

  private def createEntities(files: Seq[File]): Seq[Entity] = files.map(f => parse(f)).flatten



  private def parse(file: File): Seq[Entity] = {
    val xml: Elem = XML.loadFile(file)
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
        val positionComponent = Position.fromXML(transformation)

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
        val childPos      = Position.fromXML(mesh)
        val childDisplay  = new Display(meshId, shaderId)

        //add position, display and parentEntity to child entity
        childEntity.add(childPos)
        childEntity.add(childDisplay)
        childEntity.add(parentComp)

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
