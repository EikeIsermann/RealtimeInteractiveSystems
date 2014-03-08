package main.scala.io

import com.ardor3d.extension.model.collada.jdom._
import com.ardor3d.util.resource._
import java.io.{FileOutputStream, File}
import com.ardor3d.math.Quaternion


/**
 * Created by Christian Treffs
 * Date: 04.03.14 14:00
 */
object ColladaTest {




  def main(args: Array[String]) {
    /*
    val Spectre = new File("/Users/ctreffs/Desktop/3DModels/T-90/T-90.dae")
    val uri = Spectre.toURI
    val resLocater = new SimpleResourceLocator(uri.resolve("/Users/ctreffs/Desktop/3DModels/T-90/"))
    ResourceLocatorTool.addResourceLocator(ResourceLocatorTool.TYPE_MODEL, resLocater)
    val ci = new ColladaImporter()
    ci.setLoadTextures(true)
    val col = ci.load(uri.toString())

      */




    val filePaths: Seq[(String,String)] = Seq(
      ("SkyBox", "/Users/ctreffs/Desktop/3DModels/SkyBox/SkyBox.dae"),
      ("CompanionCube", "/Users/ctreffs/Desktop/3DModels/TexturedCube/CompanionCube.dae"),
      ("Tank","/Users/ctreffs/Desktop/3DModels/T-90/T-90.dae"),
      ("PhoneBooth","/Users/ctreffs/Desktop/3DModels/Phone_Booth/PhoneBooth.dae"),
      ("Bush","/Users/ctreffs/Desktop/3DModels/Bush/Bush.dae"),
      ("Roads","/Users/ctreffs/Desktop/3DModels/roads/Roads.dae"),
      ("SimpleCube","/Users/ctreffs/Desktop/3DModels/SimpleCube/SimpleCube.dae")
    )




    val colladas = Collada.load(filePaths)



   /* colladas foreach(
      c => {

        c.meshes foreach(mesh => {
          println(mesh.initialName, "n"+mesh.normals.length, "p"+mesh.positions.length, "t"+mesh.texCoords.length, mesh.hasInitialTransformation, mesh.vcount.keys)

        })
      }
      )

     */

    /*
    colladas.foreach(c =>
      println(
        c.filePath,
        c.authors,
        c.created.toLocaleString,
        c.modified.toLocaleString,
        c.schemaVersion,
        c.upAxis,
        c.schemaVersion,
        c.unit
      )
    ) */




  }
}
