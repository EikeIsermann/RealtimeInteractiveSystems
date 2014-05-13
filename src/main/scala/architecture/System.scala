package main.scala.architecture

import main.scala.systems.input.SimulationContext
import scala.Predef._
import main.scala.nodes.CamControlNode
import main.scala.engine.GameEngine

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
  var priority: Integer
  var active = false
  var family: Family = _


  def initialize(): System = {
    family = new Family(node)
    GameEngine.registerFamily(family)
    active = true
    this
  }


  def begin(): Unit

  def end(): Unit

  def update(context: SimulationContext): System
  //def update(nodeType: Class[_ <: Node], context: Context): System

  final def process(context: SimulationContext){
    if(active){
      begin()
      update(context)
      end()
    }

  }


  def getNodes: List[Node] = family.nodes.toList

}


abstract class DelayedProcessingSystem extends System {
  var delay: Float
  var running: Boolean
  var acc: Float


}

abstract class ProcessingSystem extends System {


}

abstract class IntervalProcessingSystem extends System {


}

abstract class VoidProcessingSystem extends System {


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