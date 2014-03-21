package main.scala.engine

import main.scala.architecture.Engine
import main.scala.event._
import akka.actor.Actor
import main.scala.tools.{ActorsInterface, Actors}

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:34
 */
object GameEngine extends Engine with Actor with ActorsInterface {

  override def init(): Engine = ???

  override def mainLoop(): Unit = ???

  override def receive: Receive = {
    case ComponentAdded(entity) => componentAdded(entity)
    case ComponentRemoved(entity) => //TODO
  }


}

