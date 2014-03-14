package main.scala.architecture

import akka.actor.Actor
import main.scala.input.Context
import main.scala.event.ObservingActor

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:16
 * System: "Each System runs continuously (as though each System had its own private thread) and performs global
 * actions on every Entity that possesses a Component of the same aspect as that System."
 */
trait System extends ObservingActor {

  /**
   * init the system
   * @return
   */
  def init(): System

  def += (component: Component): System
  def -= (component: Component): System

  def update(context: Context)

}
