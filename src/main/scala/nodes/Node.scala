package main.scala.nodes

import main.scala.architecture.Node
import main.scala.components.{Display, Position}

/**
 * Created by Christian Treffs
 * Date: 20.03.14 21:17
 */
object Node {

}

case class RenderNode(position: Position, display: Display) extends Node



/*
 BulletCollisionNode
  Bullet
  Position
  Motion

EnemyCollisionNode
  Enemy
  Life
  Position
  Motion

GunControlNode
  Gun
  HeroControl

HeroCollisionNode
  Hero
  Position

HeroControlNode
  HeroControl
  Position
  Motion

LivingNode
  Life

MovementNode
  Motion
  Position

PredatorNode (similar to PreyNode and StalkerNode)
  Predator
  Position
  Motion
  Life

RenderNode
  Display
 Position

 */