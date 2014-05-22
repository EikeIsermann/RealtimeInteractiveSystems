package main.scala.systems.gfx

import main.scala.math.Mat4f

/**
 * Created by Christian Treffs
 * Date: 22.05.14 09:27
 */
trait DrawFunction {

  def draw(shader: Shader, modelTransformation: Mat4f, viewMatrix: Mat4f, fov: Float,aspect:Option[Float],zNear:Float,zFar:Float, beforeFunc: Unit => Unit = {Unit => Unit}, afterFunc: Unit => Unit = {Unit => Unit})
}
