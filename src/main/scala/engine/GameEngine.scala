package main.scala.engine

import main.scala.architecture.{System, Engine}
import main.scala.systems.input.Context
import scala.collection.mutable.ArrayBuffer
import main.scala.components.Component
import akka.actor.Actor._
import main.scala.event.ComponentAdded
import scala.swing.event.ComponentRemoved
import akka.actor.{Actor, Props, ActorSystem}
import main.scala.tools.Actors

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:34
 */
object GameEngine extends Engine with Actor {

  final val actorRef = Actors.init(this)

  override def init(): Engine = ???

  override def mainLoop(): Unit = ???

  override def receive: Receive = {
    case ComponentAdded(entity) => componentAdded(entity)
    case ComponentRemoved(entity, component) => //TODO
  }


}

