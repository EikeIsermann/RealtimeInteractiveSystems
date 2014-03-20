package main.scala.io

import main.scala.architecture.Entity
import scala.xml.{Node, NodeSeq, XML, Elem}
import java.io.File
import main.scala.io.FileIO
import main.scala.math.Vec3f
import main.scala.systems.gfx.Mesh
import scala.collection.mutable.ListBuffer
import main.scala.components.Position


/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:31
 */
object EntityDescLoader {


  def load(pathToDir: String, extension: String = "xml") {
    val files = FileIO.loadAll(extension, pathToDir)
    val entities: Seq[Entity] = createEntities(files)
  }

  private def createEntities(files: Seq[File]): Seq[Entity] = files.map(f => parse(f))



  private def parse(file: File): Entity = {
    val xml: Elem = XML.loadFile(file)
    val name: String = (xml \ "name").text
    val graphicxComponent = xml \ "components" \ "graphics"
    val physicsComponent = xml \ "components" \ "physics"
    val aiComponent = xml \ "components" \ "ai"
    val logicComponent = xml \ "components" \ "logic"

    val entity: Entity = null


    // READ GFX
    graphicxComponent foreach(
      gc => {
        val uses: Seq[Node] =gc.nonEmptyChildren.filter(_.label == "use").map(n => {(xml \ n.text).head})
        val has: Seq[Node] = gc.nonEmptyChildren.filter(_.label != "use")
        val attrs = NodeSeq.fromSeq(uses ++ has)


        val (position, rotation, scale) = parseTransformation(attrs \\ "transformation")

        val meshNames = parseMeshes(Symbol(name), attrs \\ "meshes")


        // TODO: GFX Object

      })


    //READ Physics
    physicsComponent foreach(
      pc => {

      }
      )




    entity
  }


  def parseMeshes(identifier: Symbol, meshes: NodeSeq): Seq[Symbol] = {

    val sourceStr = (meshes \ "source").text

    Collada.load(identifier, sourceStr)

    val ret = ListBuffer.empty[Symbol]
    val ms = meshes \\ "mesh"
    ms.foreach(
      mesh => {
        val id = Symbol(mesh.attribute("id").get.text)

        ret += id
        val (pos, rot, sc) = parseTransformation(mesh)
        val m = Mesh.getByName(id)
        m.relativePosition = pos
        m.relativeRotation = rot
        m.relativeScale = sc

    })



    ret.toSeq
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
