package main.scala.systems.input

import main.scala.tools.DisplayManager
import main.scala.math.Mat4f
import main.scala.systems.gfx.Shader
import main.scala.engine.GameEngine
import main.scala.components.Camera

/**
 * Created by Christian Treffs
 * Date: 10.12.13 11:17
 *
 * the entire game state
 */
class SimulationContext {

  private var camMat = Mat4f.identity

  def setCameraIsAt(mat4f: Mat4f) = {
    camMat = mat4f
  }
  def camIsAt: Mat4f = camMat


  var shader: Shader = null
  var modelMatrix: Mat4f = Mat4f.identity
  var viewMatrix: Mat4f = Mat4f.identity

  private var _aspect: Option[Float] = Camera.defaultAspect
  private var _fieldOfView: Float = Camera.defaultFOV
  private var _nearPlane: Float = Camera.defaultNearPlane
  private var _farPlane: Float = Camera.defaultFarPlane


  def aspect:Option[Float] = _aspect
  def aspect_=(a:Option[Float]) = _aspect = a

  def fieldOfView: Float = _fieldOfView
  def fieldOfView_=(fov:Float) = _fieldOfView = fov

  def nearPlane: Float = _nearPlane
  def nearPlane_=(nP: Float) = _nearPlane = nP

  def farPlane: Float = _farPlane
  def farPlane_=(fP: Float) = _farPlane = fP



  private var _deltaT: Float = 0f


  def setViewMatrix(mat: Mat4f): Mat4f = {
    viewMatrix = mat
    viewMatrix
  }

  def setModelMatrix(mat: Mat4f): Mat4f = {
    modelMatrix = mat
    modelMatrix
  }

  def updateDeltaT(v: Float): Float = {
    _deltaT = v

    deltaT
  }

  def deltaT = _deltaT
}
