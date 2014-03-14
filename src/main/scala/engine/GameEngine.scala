package main.scala.engine

import main.scala.architecture.{System, Engine}
import main.scala.input.Context
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:34
 */
object GameEngine extends Engine {


  private val _systems: ArrayBuffer[System] = ArrayBuffer.empty[System]


  override def init() {

  }

  override def mainLoop(): Unit = {

  }

  override def update(systemType: Class[_ <: System], context: Context): Unit = _systems.filter(_.getClass == systemType).foreach(_.update(context))

  def systems(): Seq[System] = _systems.toSeq

  override def +=(system: System): Engine = {
    _systems += system
    this
  }

  override def -=(system: System): Engine = {
    _systems -= system
    this
  }


}
