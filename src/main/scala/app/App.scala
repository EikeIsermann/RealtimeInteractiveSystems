package main.scala.app

import akka.actor.{Props, ActorSystem}
import main.scala.core.events.{MyActor, Signal, ObserveSignal, TestMessage}
import ogl.app.Input
import main.scala.tools.DC

/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:39
 */
object App extends ogl.app.App{


  def main(args: Array[String]) {

    DC.debugLevel = 0



    val system = ActorSystem("mySystem")

    implicit val myActor = system.actorOf(Props[MyActor], "myActor")

    myActor ! TestMessage("hallo")

    val sig = new Signal[Int](2)

    val func = {x: Int => }

    myActor ! ObserveSignal(sig)(func)

    sig.update(234)

    sig.update(456)


  }

  def init(): Unit = ???

  def simulate(elapsed: Float, input: Input): Unit = ???

  def display(width: Int, height: Int): Unit = ???
}
