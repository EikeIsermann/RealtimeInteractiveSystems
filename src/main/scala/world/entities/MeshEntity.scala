package main.scala.world.entities

import main.scala.systems.input.SimulationContext
import main.scala.io.Mesh

/**
 * Created by Christian Treffs
 * Date: 13.03.14 12:10
 */
class MeshEntity(mesh: Mesh) extends Entity {

  /**
   * get the lifetime of the entity
   * lifetime <= 0 equals infinite lifetime
   * @return lifetime in milliseconds
   */
  override def lifetime: Long = 1L

  /**
   * destruction process
   */
  override def destroy(): Boolean = true

  /**
   * physics simulation process
   */
  override def simulate(context: SimulationContext): Boolean = true

  /**
   * creation and initialisation process
   */
  override def init(context: SimulationContext): Boolean = true

  /**
   * get the entity's name
   * @return the name
   */
  override def name: String = mesh.name.toString()

  /**
   * rendering process
   */
  override def render(context: SimulationContext): Boolean = {
    mesh.draw(Mesh.defaultShader, context.modelMatrix, context.projectionMatrix, context.viewMatrix)
    true
  }
}
