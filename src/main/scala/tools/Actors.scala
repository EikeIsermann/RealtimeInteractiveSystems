package main.scala.tools

import akka.actor.{Actor, Props, ActorRef, ActorSystem}
import scala.reflect.ClassTag

/**
 * Created by Christian Treffs
 * Date: 21.03.14 13:53
 */
object Actors {
  final val actorSystem = ActorSystem.create()

  def init[T <: Actor: ClassTag](toBeActor: T) = actorSystem.actorOf(Props(toBeActor))
}
