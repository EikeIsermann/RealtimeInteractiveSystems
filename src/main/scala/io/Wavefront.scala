package main.scala.io

import scala.io.Source
import scala.collection.mutable._
import scala.collection.mutable
import main.scala.io.Mtl.Material

/**
 * Created by Christian Treffs
 * Date: 10.04.14 08:34
 */
object Wavefront {

  case class Vec2(u: Float,v: Float)
  case class Vec3(x: Float, y: Float, z: Float)
  case class MeshBuffer(name: String) {
    val position = ArrayBuffer.empty[Float]
    val normals = ArrayBuffer.empty[Float]
    val texCoords = ArrayBuffer.empty[Float]
    var polySize: Int = 0
    var texture: String = ""
  }



  final val separator: String = " "
  final val faceSeparator: String = "/"
  //val objFile = FileIO.load("/Users/ctreffs/Downloads/Paris/Paris2010_0.obj")

  //val objFile = FileIO.load("/Users/ctreffs/Downloads/cubus_bw_faun_912-21/cubus_faun_912_21.obj")

 val objFile = FileIO.load("/Users/ctreffs/Downloads/Su-37_Terminator/Su-37_Terminator.obj")


  val vertexBuffer: ArrayBuffer[Vec3] = ArrayBuffer.empty[Vec3]
  val normalsBuffer: ArrayBuffer[Vec3] = ArrayBuffer.empty[Vec3]
  val texCoordsBuffer: ArrayBuffer[Vec2] = ArrayBuffer.empty[Vec2]
  var currentMeshBuffer: MeshBuffer = null
  var currentMaterial: Material = null


  def main(args: Array[String]) {


    for(l <- Source.fromFile(objFile).getLines()) {
      if(l.length > 1) {
        val line = l.trim
        val s = line.split(separator,2)
        val prefix = s(0)

        prefix match {
          case "vt" => parseTexCoords(s(1))
          case "vn" => parseNormals(s(1))
          case "vp" => // Parameter space vertices in ( u [,v] [,w] ) form; free form geometry statement ( see below )
          case "v" => parseVertices(s(1))
          case "f" => parseFaces(s(1))
          case "o" =>  tab("ObjectName "+s(1))//object name
          case "g" =>  createNewGroup(s(1))
          case "mtllib" => loadMaterial(s(1)) //Materials that describe the visual aspects of the polygons are stored in external .mtl files. More than one external MTL material file may be referenced from within the OBJ file. The .mtl file may contain one or more named material definitions.
          case "usemtl" => useMaterial(s(1))
          case "s" => tab("SmoothShading") //Smooth shading across polygons is enabled by smoothing groups.
          case "#" => //comment
          case _ => println(line)
        }
      }
    }
  }


  def createNewGroup(str: String) {
    val name = str.trim
    currentMeshBuffer = new MeshBuffer(name)
    println("new mesh '"+name+"'")

  }

  def loadMaterial(str: String) {
    Mtl.load(FileIO.getPath(objFile)+"/"+str.trim)

  }

  def useMaterial(str: String) {
    // This tag specifies the material name for the element following it. The material name matches a named material definition in an external .mtl file.
    currentMaterial = Mtl.materials(str.trim)
    if(!currentMaterial.diffuseTextureMap.isEmpty) {
      currentMeshBuffer.texture =  currentMaterial.diffuseTextureMap
      return
    }
    if(!currentMaterial.ambientTextureMap.isEmpty) {
      currentMeshBuffer.texture =  currentMaterial.ambientTextureMap
      return

    }

    if(!currentMaterial.specularColorTextureMap.isEmpty) {
      currentMeshBuffer.texture =  currentMaterial.specularColorTextureMap
      return
    }


  }

  def parseVertices(str: String) {
    // List of Vertices, with (x,y,z[,w]) coordinates, w is optional and defaults to 1.0.
    val v = str.split(separator)
    vertexBuffer += Vec3(v(0).toFloat,v(1).toFloat,v(2).toFloat)
  }

  def parseTexCoords(str: String) {
    //Texture coordinates, in (u ,v [,w]) coordinates, these will vary between 0 and 1, w is optional and default to 0.
    val vt = str.split(separator)
    texCoordsBuffer += Vec2(vt(0).toFloat, vt(1).toFloat)
  }

  def parseNormals(str: String) {
    // Normals in (x,y,z) form; normals might not be unit.
    val vn = str.split(separator)
    normalsBuffer += Vec3(vn(0).toFloat, vn(1).toFloat, vn(2).toFloat)
  }

  def parseFaces(str: String) {
      val polys = str.split(separator)

      // triangle / quad etc.
      val polygonSize: Int = polys.length
      currentMeshBuffer.polySize = polygonSize

      polys.foreach(face => {
        val f = face.split(faceSeparator)
        f.length match {
          case 1 =>
            // f v1 v2 v3 v4 ....
            if(!f(0).isEmpty) {
              val v: Vec3 = vertexBuffer(f(0).toInt-1)
              currentMeshBuffer.position += v.x
              currentMeshBuffer.position += v.y
              currentMeshBuffer.position += v.z
            }
          case 2 =>
            // f v1/vt1 v2/vt2 v3/vt3 ...
            if(!f(0).isEmpty) {
              val v: Vec3 = vertexBuffer(f(0).toInt-1)
              currentMeshBuffer.position += v.x
              currentMeshBuffer.position += v.y
              currentMeshBuffer.position += v.z
            }
            if(!f(1).isEmpty) {
              val t = texCoordsBuffer(f(1).toInt-1)
              currentMeshBuffer.texCoords += t.u
              currentMeshBuffer.texCoords += t.v
            }
          case 3 =>
            // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ....
            // f v1//vn1 v2//vn2 v3//vn3 ...
            if(!f(0).isEmpty) {
              val v: Vec3 = vertexBuffer(f(0).toInt-1)
              currentMeshBuffer.position += v.x
              currentMeshBuffer.position += v.y
              currentMeshBuffer.position += v.z
            }
            if(!f(1).isEmpty) {
              val t = texCoordsBuffer(f(1).toInt-1)
              currentMeshBuffer.texCoords += t.u
              currentMeshBuffer.texCoords += t.v
            }
            if(!f(2).isEmpty) {
              val n = normalsBuffer(f(2).toInt-1)
              currentMeshBuffer.normals += n.x
              currentMeshBuffer.normals += n.y
              currentMeshBuffer.normals += n.z
            }
          case _ => throw new IllegalArgumentException("can't parse face type '"+f+"'")
        }
      })

  }

  var t = ""
  def tab(str: String, newMesh: Int = 0) {
    if(t != str) {
      if(newMesh == 1) {
        println()
      }
      if(newMesh == 3) {
        println("------------ NEW MESH -------")
      }

      println(str)
      t = str
      if(newMesh == 2) {
        println()
      }
    }
  }


}

object Mtl {

  case class Material() {
    var diffuseTextureMap: String = ""
    var ambientTextureMap: String = ""
    var specularColorTextureMap: String = ""
  }


  val materials = mutable.HashMap.empty[String,Material]
  var currentMaterial: String = ""
  final val separator: String = " "


  def load(filePath: String) {

  val mtlFile = FileIO.load(filePath)




  for(l <- Source.fromFile(mtlFile).getLines()) {
    if(l.length > 1) {
      val line = l.trim
      val s = line.split(separator, 2)
      val prefix = s(0)

      if (s.length > 1) {
      prefix.toLowerCase match {
        case "newmtl" => createNewMaterial(s(1))
        case "map_ka" => materials(currentMaterial).ambientTextureMap = s(1)//the ambient texture map
        case "map_kd" => materials(currentMaterial).diffuseTextureMap = s(1)// the diffuse texture map (most of the time, it will be the same as the ambient texture map)
        case "map_ks" => materials(currentMaterial).specularColorTextureMap = s(1)// specular color texture map
        case "map_ns" => // specular highlight component
        case "map_d" => // the alpha texture map
        case "map_bump" => "some implementations use 'map_bump' instead of 'bump' below"
        case "bump" => "some implementations use 'map_bump' instead of 'bump' below"
        case _ =>

      }
      }
    }
  }
  }

  def createNewMaterial(str: String) {
    val name = str.trim
    materials.put(name, new Material())
    currentMaterial = name

  }

}
