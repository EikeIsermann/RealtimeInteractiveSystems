package main.scala.core.events


/**
 * Created by Christian Treffs
 * Date: 19.11.13 16:42
 */
class MyActor() extends ObservingActor {

  protected def unknownMessage(msg: Any): Unit = {
    println(msg)
  }
}