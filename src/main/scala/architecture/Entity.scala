package main.scala.architecture

/**
 * Created by Christian Treffs
 * Date: 17.03.14 12:52
 */
trait Entity {

  def +=(component: Component): Boolean

  def -=(component: Component): Boolean

}
