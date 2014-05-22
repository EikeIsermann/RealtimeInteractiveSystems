package main.scala.systems.gameplay.states

import main.scala.architecture.System
/**
 * Created by Christian Treffs
 * Date: 22.05.14 11:44
 */
trait GameState {

  def execute()(implicit owner: System): GameState

}
