package main.scala.io

import scala.xml._
import java.io._
import main.scala.tools.DC
import java.text.SimpleDateFormat
import java.util.Date
import main.scala.math.Vec3f
import scala.reflect.ClassTag
import scala.collection.mutable.ListBuffer
import main.scala.systems.gfx.{Texture, Mesh}

/**
 * Created by Christian Treffs
 * Date: 04.03.14 12:22
 *
 * Collada Schema used: http://www.collada.org/2005/11/COLLADASchema/
 */
object Collada {



    final val fileExtension = "dae"
    final val schemaURL =   "http://www.collada.org/2005/11/COLLADASchema"
    final val schemaVersion: String =  "1.4.1"
    final val dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss"
    final val separator: String = " "

    def load(filePathsAndIds: Map[Symbol, String]): Seq[Collada] = filePathsAndIds.map(x => load(x._1,x._2)).toSeq
    def load(filePathsAndIds: Seq[(Symbol, String)]): Seq[Collada] = filePathsAndIds.map(l => load(l._1, l._2))

    def load(identifier: Symbol, filePath: String): Collada = {
      if(filePath == null || filePath.isEmpty) {
        throw new IllegalArgumentException("[ERROR] Can not import File '"+filePath+"'")
      }
      load(identifier, new File(filePath))
    }

    def load(identifier: Symbol, file: File): Collada =  {
      val start = System.currentTimeMillis()
      if(!file.exists || !file.canRead || !file.isFile) {
        throw new IllegalArgumentException("[ERROR] Can not import File '"+file.getAbsoluteFile+"'")
      }
      if(!file.getAbsoluteFile.toString.endsWith(fileExtension)) {
        DC.warn("file extension is not '"+fileExtension+"'", file.getAbsoluteFile)
      }


      val cf = new Collada(identifier, file)

      // init all meshes!!!
      cf.meshes.map(_.setBuffers())

      val end = System.currentTimeMillis()
      DC.log("Collada imported in "+(end-start)+" ms", "GroupId: '"+identifier+"'\tMeshes: "+cf.meshes.map(_.name)+" \t FilePath: "+file.getAbsoluteFile)

      cf
    }

}

object ColladaSemantic extends Enumeration {
  type ColladaSemantic = Value
  val BINORMAL, CONTINUITY, IMAGE, INPUT, WEIGHT, INTERPOLATION, INV_BIND_MATRIX, UV, VERTEX, JOINT, LINEAR_STEPS, NORMAL, OUTPUT, TEXCOORD, POSITION, MORPH_TARGET, MORPH_WEIGHT, TEXTANGENT, TANGENT = Value
}

sealed class Collada(identifier:Symbol, colladaFile: File) {

  val groupId: Symbol = identifier
  private val f: File = colladaFile
  private val xml: Elem = XML.loadFile(f)
  private val df: SimpleDateFormat = new SimpleDateFormat(Collada.dateFormat)

  final val filePath: String = f.getAbsoluteFile.toString

  final val schemaURL: String = xml.namespace
  final val schemaVersion: String = xml.attributes.get("version").getOrElse("").toString

  // test if schema is valid
  schemaIsValid()


  // asset
  private val asset = xml \ "asset"
    private val contributors = asset \ "contributor"

  final val created: Date = parseDate(asset \ "created" text)
  final val modified: Date = parseDate(asset \ "modified" text)

  final val upAxis: Vec3f = parseUpAxis(asset \ "up_axis" text)



  final val unit: Option[(Float, String)] = parseUnit(asset \ "unit")


  private val scene_nodes = xml \ "library_visual_scenes" \ "visual_scene" \ "node"

  val meshes = parseScene(scene_nodes)




  def schemaIsValid(): Boolean = {
    if(schemaURL != Collada.schemaURL) {
      throw new IllegalArgumentException("Collada file has incompatible Schema '"+schemaURL+"'; only '"+Collada.schemaURL+"' is supported")
    }
    if(schemaVersion != Collada.schemaVersion) {
      DC.warn("Collada Schema Version is not '"+Collada.schemaVersion+"'; Version is: '"+schemaVersion+"'; This can have unforeseen consequences", f.getAbsoluteFile)
    }
    false
  }


  def authors: Seq[String] = {
    contributors.map(c => (c \ "author").text).toSeq
  }

  def parseScene(sceneNodes: NodeSeq): Seq[Mesh] = {



    val meshes = new ListBuffer[Mesh]()
    sceneNodes foreach { node => {
      val mesh = new Mesh(groupId, node.attribute("id").get.text, node.attribute("name").get.text)
      (node \ "matrix") foreach(
        tNode => {
          val trafo = parseToArray[Float](tNode.text.trim)
          mesh.setInitialTransformation(trafo)
        }
        )



      (node \ "instance_geometry").foreach( geo =>  {
          val geoUrl = geo.attribute("url").get.text

          // geo the geometry
          (xml \\ "geometry" filter {_ \\ "@id" exists (_.text == geoUrl.replace("#", ""))}) foreach { geoNode => {
            //println(mesh.initialName, mesh.initialTransformation.toList)
            meshes += parseMesh(geoNode, mesh)
            }
          }


          //bind the material
        (geo \\ "instance_material" ).foreach(
          mat => {
            val target: String = mat.attribute("target").get.text
            //val symbol: String = mat.attribute("symbol").get.text

            (xml \ "library_materials" \\ "material" filter {_ \\ "@id" exists (_.text == target.replace("#", ""))}) foreach {
              mat => {
                //val id: String = mat.attribute("id").get.text
                //val name: String = mat.attribute("name").get.text

                (mat \ "instance_effect") foreach {
                  ie => {
                    val url = ie.attribute("url").get.text
                    (xml \ "library_effects" \\ "effect" filter {_ \\ "@id" exists (_.text == url.replace("#", ""))}) foreach {
                      eff => {
                        //val id = eff.attribute("id").get.text
                        (eff \ "profile_COMMON" \ "newparam" \ "surface" filter {_ \\ "@type" exists (_.text == "2D")}) foreach {
                          surf => {
                            (surf \ "init_from") foreach {
                              init => {
                                val texId: String = init.text
                                (xml \ "library_images" \\ "image" filter {_ \\ "@id" exists (_.text == texId)}) foreach {
                                  img => {
                                    (img \ "init_from") foreach {
                                      tex => {
                                        val textureFilePath: String = FileIO.getPath(colladaFile)+tex.text
                                        val texture = Texture.load(textureFilePath)

                                        mesh.setTexId(texture.texId)
                                      }


                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }

              }
            }

          }

          )

        })
      }
    }

    meshes.toSeq
  }




  //HELPER
  private def parseDate(string: String): Date = {
    if(string == null || string.isEmpty) {
      return null
    }
    df.parse(string)
  }

  private def parseUpAxis(string: String): Vec3f = {
    string match {
      case "X_UP" => Vec3f(1,0,0)
      case "Y_UP" => Vec3f(0,1,0)
      case "Z_UP" => Vec3f(0,0,1)
      case _ =>
        DC.warn("No UP_AXIS defined in Collada file", filePath)
        Vec3f(0,0,0)
    }
  }


  private def parseUnit(unit: NodeSeq): Option[(Float, String)] = {
    unit collectFirst {
      case el: Elem => return Some(((unit \ "@meter" text).toFloat, (unit \ "@name").text))
      case _ => return None
    }
  }





  def parseMesh(geo: Node, mesh: Mesh): Mesh = {
    val meshNode = geo \ "mesh"

    //parse vertices
    meshNode \\ "vertices" foreach {vs => parseVertices(vs, mesh) }
    meshNode \\ "triangles" foreach {ts => parseTriangles(ts, mesh) }
    meshNode \\ "polylist" foreach {pl => parsePolylist(pl, mesh) }
    meshNode \\ "polygons" foreach {pg => parsePolygons(pg, mesh) }


    mesh
  }




  def parseVertices(vertices: Node, mesh: Mesh): Mesh = {
    vertices foreach {
      verticesRefNode => verticesRefNode \\ "input"  foreach {
        inputNode => {
          val (node, semantic, offset) = parseInput(inputNode)
          mesh.setOffset(semantic, offset)
          parseSource(node, semantic, mesh)
        }
      }
    }
    mesh
  }
  def parseTriangles(triangles: Node, mesh: Mesh): Mesh = {
    val count = triangles.attribute("count").get.text.toInt
    val material = triangles.attribute("material").get.text.toString
    val p = (triangles \ "p" ).text.toString
    mesh.setIndex(parseToArray[Int](p, count), material)
    mesh.isTriangles = true
    triangles foreach {
      trianglesRefNode => trianglesRefNode \\ "input" foreach {
        inputNode => {
          val (node, semantic, offset) = parseInput(inputNode)
          mesh.setOffset(semantic, offset)
          parseSource(node, semantic, mesh)
        }
      }
    }
    mesh
  }
  def parsePolylist(polylist: Node, mesh: Mesh): Mesh = {
    val count = polylist.attribute("count").get.text.toInt
    val material = polylist.attribute("material").get.text.toString

    val vcount = (polylist \ "vcount" ).text.toString
    val p = (polylist \ "p" ).text.toString

    mesh.setVcount(parseToArray[Int](vcount, count), material)
    mesh.setIndex(parseToArray[Int](p), material)

    mesh.isPolyList = true
    polylist foreach {
      polylistRefNode => polylistRefNode \\ "input" foreach {
        inputNode => {
          val (node, semantic, offset) = parseInput(inputNode)
          mesh.setOffset(semantic, offset)
          parseSource(node, semantic, mesh)
        }
      }
    }
    mesh
  }
  def parsePolygons(polygons: Node, mesh: Mesh): Mesh = {
    //val count = polygons.attribute("count").get.text.toInt
    //val material = polygons.attribute("material").get.text.toString
    mesh.isPolygons = true
    polygons foreach {
      polygonsRefNode => polygonsRefNode \\ "input" foreach {
        inputNode => {
          val (node, semantic, offset) = parseInput(inputNode)
          mesh.setOffset(semantic, offset)
          parseSource(node, semantic, mesh)
        }
      }
    }
    mesh
  }


  def parseInput(inputNode: Node): (Node, String, Int) = {
    val (semantic, sourceId) = (inputNode.attribute("semantic").get.toString(), inputNode.attribute("source").get.toString())

    var offset: Int = -1
    inputNode.attribute("offset").foreach(o => offset = o.text.toInt)

    var node: Node = null
    (xml \\ "source" filter {_ \\ "@id" exists (_.text == sourceId.replace("#", ""))}) foreach { n =>
      node = n
    }
    (xml \\ "vertices" filter {_ \\ "@id" exists (_.text == sourceId.replace("#", ""))}) foreach {n =>
      n \ "input" foreach {p =>
        node = p
      }
    }
    (node, semantic, offset)
  }



  def parseSource(source: Node, semantic: String, mesh: Mesh): Mesh = {



    val floatArrayNodes = source \ "float_array"

    floatArrayNodes foreach {
      floatArrayNode => {

        val id: String = floatArrayNode.attribute("id").get.toString()
        val count: Int = floatArrayNode.attribute("count").get.toString().toInt
        val str: String = floatArrayNode.text

        val arr: Array[Float] = parseToArray[Float](str, count)

        val accessor = (source \\ "accessor" filter {_ \\ "@source" exists (_.text == "#" + id)}).head

        val strideCount = accessor.attribute("count").get.text.toInt
        val stride = accessor.attribute("stride").get.text.toInt

        ColladaSemantic.withName(semantic.toUpperCase) match {
          case ColladaSemantic.NORMAL => mesh.setNormals(arr, stride, strideCount)
          case ColladaSemantic.VERTEX => //mesh.setVertices(arr, stride, strideCount)
          case ColladaSemantic.POSITION => mesh.setPositions(arr, stride, strideCount)
          case ColladaSemantic.TEXCOORD => mesh.setTexCoords(arr, stride, strideCount)
          case _ => throw new IllegalArgumentException("Can not handle strange type")
        }
      }
    }


    mesh

  }


  def parseToArray[T : ClassTag](string: String, entryCount: Int = -1, separator: String = Collada.separator): Array[T] = {

    val strArr = string.trim().split(separator)
    val count = if(entryCount == -1) strArr.length else entryCount
    val ret: Array[T] = Array.ofDim[T](count)
    for (i <- 0 until count) {
      ret(i) =  parseTo(strArr(i)).asInstanceOf[T]
    }

    def parseTo(string: String): Any = {

      ret match {
        case _: Array[Double] => string.toDouble
        case _: Array[Float] => string.toFloat
        case _: Array[Int] => string.toInt
        case _: Array[Long] => string.toLong
        case _: Array[Boolean] => string.toBoolean
        case _: Array[Byte] => string.toByte
        case _ => throw new IllegalArgumentException("can not parse string to array of given type")
      }
    }

    if(ret.length != count) {
      throw new IllegalArgumentException("Arrays not equal")
    }
    ret
  }

}




