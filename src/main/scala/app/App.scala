package main.scala.app

import akka.actor.{Props, ActorSystem}
import main.scala.actors.MyActor
import main.scala.eventsystem.{ObserveSignal, Signal, TestMessage}

/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:39
 */
object App {


  def main(args: Array[String]) {

    val system = ActorSystem("mySystem")

    implicit val myActor = system.actorOf(Props[MyActor], "myActor")

    myActor ! TestMessage("hallo")

    val sig = new Signal[Int](2)

    val func = {x: Int => println("value" +x)}

    myActor ! ObserveSignal(sig)(func)

    sig.update(234)

    sig.update(456)


  }

}
