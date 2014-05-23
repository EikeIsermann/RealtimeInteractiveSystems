package main.scala.systems.ai.aiStates

import main.scala.architecture.{Node, System}
import main.scala.nodes.GunAINode
import main.scala.components.{Gun, Placement}
import main.scala.entities.Entity
import main.scala.math.{Vec3f, RISMath}

/**
 * User: uni
 * Date: 22.05.14
 * Time: 18:23
 * This is a RIS Project class
 */
abstract class AIState {
  def execute(node: Node)(implicit owner: System): AIState
}

class gunSearching() extends AIState{

  var nextState: AIState = this
  def execute(node: Node)(implicit owner: System): AIState = {
    nextState
  }

}
class gunTargetAcquired(enemy: Entity) extends AIState {
  var nextState: AIState = this
  var e = enemy
  def execute(node: Node)(implicit owner: System): AIState = {
    node match {
      case gai: GunAINode =>
        val pos = gai -> classOf[Placement]
        val gun = gai -> classOf[Gun]
        if (!e.destroying) {
        val target: Vec3f = e.getComponent(classOf[Placement]).getMatrix.position
        val vectorAim: Vec3f = (target - pos.getMatrix.position).norm
        val vectorNow: Vec3f = RISMath.DirFromRot(pos.getUnscaledMatrix.rotation * pos.rotation).norm

        val angle = math.acos((vectorAim ° vectorNow) / vectorAim.magnitude * vectorNow.magnitude)
        val cross: Vec3f = vectorAim % vectorNow

        val angleDeg: Float = math.toDegrees(angle).toFloat
        val factor: Float = 0.5f
        val turretRot: Float = if (cross.y < 0) factor else -factor
        pos.rotation = Vec3f(pos.rotation.x, pos.rotation.y + turretRot, pos.rotation.z)
        if (angleDeg < gun.fireAngle) nextState = new gunTargetLocked(e)
    }
      case _ =>
    }
  nextState
  }

}

class gunTargetLocked(e: Entity) extends AIState {
  var nextState: AIState = this

  def execute(node: Node)(implicit owner: System): AIState = {
    node match {
      case gn: GunAINode =>
        val gun = gn -> classOf[Gun]
        //val gunai = gn -> classOf[GunAI]
          gun.shoot(s = true)
          nextState = new gunTargetAcquired(e)
      case _ =>
    }
    nextState
  }

}

