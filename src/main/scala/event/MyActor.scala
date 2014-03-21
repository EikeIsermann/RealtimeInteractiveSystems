package main.scala.event

import akka.actor._
import akka.actor.Actor.Receive
import main.scala.event.TestMessage
import main.scala.event.TestMessage


/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:42
 */


class A extends Actor {

  override def receive: Actor.Receive = {
    case t: TestMessage => println(this.getClass.getSimpleName+" received "+ t.msg)
  }
}


class B extends Actor{

  override def receive: Actor.Receive = {
    case t: TestMessage => println(this.getClass.getSimpleName+" received "+ t.msg)
  }
}

object MyActor {
  def main(args: Array[String]) {

    val sys = ActorSystem.create()
    val a = sys.actorOf(Props(new A))
    val b = sys.actorOf(Props(new B))



    a ! TestMessage("Hello A")

    b ! TestMessage("Hello B")


  }
}