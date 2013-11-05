import akka.actor.{Actor, Props, ActorSystem}

/**
 * Created by Christian Treffs
 * Date: 05.11.13 17:00
 */
object GraphImplTest {



    def main(args: Array[String]) {

      val system = ActorSystem()
      val myActor = system.actorOf(Props[MyActor])

      myActor ! TestMessage
    }

}

case class TestMessage()

class MyActor extends Actor {
  def receive: Actor.Receive = {

    case TestMessage =>
  }
}