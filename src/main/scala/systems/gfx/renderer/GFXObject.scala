package main.scala.systems.gfx.renderer

import main.scala.systems.gfx.Mesh

/**
 * Created by Eike on 20.03.14.
 */
class GFXObject {

  private val meshName: Symbol = null


  def draw() {
    val mesh  = Mesh.getByName(meshName)

    //TODO: mesh draw
  }
  def light() {}
}
