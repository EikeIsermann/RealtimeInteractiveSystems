package main.scala.architecture

import main.scala.systems.input.SimulationContext
import scala.Predef._
import main.scala.engine.GameEngine
import main.scala.event.{NodeAdded, Event, EventReceiver}

/**
 * Created by Christian Treffs
 * Date: 14.03.14 18:16
 *
 * Systems operate on lists of Nodes.
 *
 * System: "Each System runs continuously (as though each System had its own private thread) and performs global
 * actions on every Entity that possesses a Component of the same aspect as that System."
 */
abstract class System /*extends ObservingActor*/ {

  var node: Class[_ <: Node]
  var priority: Int
  var active = false
  var family: Family = _
  var ctx: SimulationContext = _

  def initialize(): System = {
    family = new Family(node)
    GameEngine.registerFamily(family)
    active = true
    this
  }


  def begin(): Unit

  def end(): Unit

  def update(): System

  //def update(nodeType: Class[_ <: Node], context: Context): System


  final def process(context: SimulationContext) {
    if (active) {
      ctx = context
      begin()
      update()
      end()
    }

  }


  def getNodes: List[Node] = family.nodes.toList

}


abstract class DelayedProcessingSystem extends System with EventReceiver {
  var delay: Float
  var running: Boolean
  var acc: Float


  def receive(ev: Event) {
    ev match {
      case na: NodeAdded =>
        if (na.family == this.family) nodeInserted(na.node)
      case _ =>
    }
  }

  def nodeInserted(node: Node) {

    val del = getRemainingDelay(node)
    if (del > 0) offerDelay(del)
  }


  def update(ctx: SimulationContext): System = {
    for (node <- getNodes) {
      processDelta(node, acc)
      val remaining = getRemainingDelay(node)
      if (remaining <= 0) {
        processExpired(node)

      }
      else offerDelay(remaining)
    }

    this
  }


  def offerDelay(del: Float) {
    if (!running || delay < getRemainingTimeUntilProcessing) {
      restart(delay)
    }
  }

  def getRemainingDelay(node: Node): Float

  final def checkProcessing: Boolean = {
    if (running) {
      acc += ctx.deltaT

      if (acc >= delay) return true
    }
    false
  }

  /**
   * Set new remaining delay for node
   * @param node the node
   * @param accDelta the accumulated delta
   */

  def processDelta(node: Node, accDelta: Float)

  /**
   * Normal entity processing if acc < delay

   */
  def processExpired(node: Node)

  def restart(del: Float) {
    delay = del
    acc = 0
    running = true
  }

  def getInitialTimeDelay: Float = delay

  def getRemainingTimeUntilProcessing: Float = {
    if (running) delay - acc
    else 0
  }

  def isRunning: Boolean = running

  def stop() {
    running = false
    acc = 0
  }


}

abstract class ProcessingSystem extends System {

  override def update() = {
    getNodes.foreach(processNode)
    this
  }

  def processNode(node: Node)

}

abstract class IntervalProcessingSystem extends System {
  var acc: Float
  val interval: Float


  def checkProcessing(ctx: SimulationContext): Boolean = {
    acc += ctx.deltaT
    if (acc >= interval) {
      acc -= interval
      true
    }
    else
      false
  }


  def update(ctx: SimulationContext) = {
    if (checkProcessing(ctx)) getNodes.foreach(processNode)
    this
  }

  def processNode(node: Node)

}

abstract class VoidProcessingSystem extends System {


  def update(ctx: SimulationContext) = {
    processSystem(ctx)
    this
  }

  def processSystem(ctx: SimulationContext)

}


object SystemPriorities {
  val Pre_Update = 1
  val Update = 2
  val Move = 3
  val ResolveCollisions = 4
  val Render = 5

}


/*

BulletRangeSystem
  Operates on BulletCollisionNodes
  Destroys bullets after a given time threshold

CollisionSystem
  Operates on EnemyCollisionNodes, BulletCollisionNodes and HeroCollisionNodes
  Runs through the various collision nodes and damages enemies etc.

EnemyRadarSystem
  Operates on EnemyCollisionNodes
  Places markers around the edge of the screen showing you where enemies are (but only when there are very few enemies left)

GunControlSystem
  Operates on GunControlNodes and EnemyCollisionNodes
  Fires bullets from your gun and auto-aims at nearby enemies

HeroControlSystem
  Operates on HeroControlNodes
  Controls player movement

LifeSystem
  Operates on LivingNodes
  Removes entities when their health reaches zero

MovementSystem
  Operates on MovementNodes
  Applies velocity to position

PredatorSystem, PreySystem and StalkerSystem
  Operates on PredatorNodes, PreyNodes and StalkerNodes
  Updates the motion and position components of these nodes to control enemies in various ways
  Adding more of these kinds of systems will make the game more fun

RenderSystem
  Operates on RenderNodes
  Updates the position and rotation of display objects

WaveSystem
  Operates on EnemyCollisionNodes and HeroCollisionNodes
  Creates waves of enemies
 */