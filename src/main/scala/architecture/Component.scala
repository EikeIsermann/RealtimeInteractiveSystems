package main.scala.architecture

import main.scala.event.ObservingActor
import main.scala.systems.input.Context

/**
 * Created by Christian Treffs
 * Date: 14.03.14 17:43
 *  the raw data for one aspect of the object, and how it interacts with the world.
 *  "Labels the Entity as possessing this particular aspect".
 *  Implementations typically use Structs, Classes, or Associative Arrays.
 */
trait Component extends ObservingActor {

  def init(): Component

  def update(context: Context)

  def deinit()
}
