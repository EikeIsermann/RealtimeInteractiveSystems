package main.scala.systems.physics

import main.scala.architecture._
import main.scala.tools.Identifier
import main.scala.components._
import scala.collection.mutable
import main.scala.components.AABB
import main.scala.nodes.CollisionNode
import main.scala.math.Vec3f
import main.scala.engine.GameEngine

/**
 * Created by Christian Treffs
 * Date: 06.05.14 15:55
 */
case class Pair(a: Identifier, b: Identifier)

class CollisionSystem extends ProcessingSystem {


  private var xAxis = mutable.ArrayBuffer[BBEndPoint]()
  private var yAxis = mutable.ArrayBuffer[BBEndPoint]()
  private var zAxis = mutable.ArrayBuffer[BBEndPoint]()
  private val pairs = mutable.HashMap[(Collision, Collision), Int]()
  private val collisions = mutable.ArrayBuffer[(Collision, Collision)]()


  override var node: Class[_ <: Node] = classOf[CollisionNode]
  override var priority = 0


  def init(): System = {
    this
  }

  override def begin(): Unit = {
    xAxis.clear()
    yAxis.clear()
    zAxis.clear()
    pairs.clear()
    collisions.clear()
  }


  override def processNode(n: Node) = {
    //update bounding box with the objects position
    n match {
      case colNode: CollisionNode =>
        val collision: Collision = colNode -> classOf[Collision]
        val placement: Placement = colNode -> classOf[Placement]


        //update bounding box with the objects position
        //println(placement.position.inline,placement.getMatrix.position.inline)

        collision.updateBoundingVolume(placement.getMatrix)

        collision.boundingVolume match {
          case a: AABB => addAABB2AxisArray(a)
          case s: Sphere => //TODO:
        }
      case _ => throw new IllegalArgumentException("not a CollisionNode")
    }

  }

  override def end(): Unit = {

    // sweep and prune collisions
    sweepAndPrune(xAxis)
    sweepAndPrune(yAxis)
    sweepAndPrune(zAxis)




    collisions ++= pairs.filter(p => {
      p._2 == 3
    }).keySet

    //TODO: find the point to collide


    collisions.foreach(pair => {
      println("COLLISION:"+pair._1 +"<->"+ pair._2)

      val c1: Collision = pair._1
      val c2: Collision = pair._2

      val e1 = GameEngine.entities(c1.owner.toString)
      val e2 = GameEngine.entities(c2.owner.toString)

      // play collision sound if there is a sound component with collision
      e1.getIfPresent(classOf[Sound]).map(_.playList += 'collision)
      e2.getIfPresent(classOf[Sound]).map(_.playList += 'collision)


    })


  }

  def deinit(): Unit = {}


  private def addAABB2AxisArray(aabb: AABB) {
    xAxis ++= aabb.interval(0)
    yAxis ++= aabb.interval(1)
    zAxis ++= aabb.interval(2)
  }


  private def sweepAndPrune(axis: mutable.ArrayBuffer[BBEndPoint]) {
    for (j <- 1 until axis.size) {
      val current: BBEndPoint = axis(j)
      var i = j - 1
      while (i >= 0 && axis(i).value > current.value) {
        //println(j,i,axis(i).value,current.value)
        val before: BBEndPoint = axis(i)

        val pair = (current.owner(), before.owner())

        if (current.isMin && !before.isMin) {
          pairs.contains(pair) match {
            case true =>
              pairs(pair) += 1
              if (pairs(pair) == 3) {
                //println("collision", before.owner(), current.owner())
                //println("collision")
              }
            case false => pairs += pair -> 1
          }
        }

        if (!current.isMin && before.isMin) {
          pairs.contains(pair) match {
            case true => pairs(pair) -= 1
            case false => throw new IllegalArgumentException("should not happen @collision")
          }
        }

        axis(i + 1) = before
        i -= 1
      }

      axis(i + 1) = current

    }
  }

  /*
 override def update(context: SimulationContext): System = {
   val start = System.currentTimeMillis()
   // 1. get all collision nodes
   val collisionNodes = Seq(
     new CollisionNode(new Collision(new AABB(Vec3f(-1,-1,-1), Vec3f(1,1,1))), new Placement(Vec3f(1,0,0))),
     new CollisionNode(new Collision(new AABB(Vec3f(-1,-1,-1), Vec3f(1,1,1))), new Placement(Vec3f(1,0,0))),
     new CollisionNode(new Collision(new AABB(Vec3f(-1,-1,-1), Vec3f(1,1,1))), new Placement(Vec3f(1,0,0))),
     new CollisionNode(new Collision(new AABB(Vec3f(-1,-1,-1), Vec3f(1,1,1))), new Placement(Vec3f(1,0,0))),
     new CollisionNode(new Collision(new AABB(Vec3f(-1,-1,-1), Vec3f(1,1,1))), new Placement(Vec3f(1,0,0)))
   )

   // 2. update their bounding boxes with the objects position
   collisionNodes.foreach(cN => cN.collision.updateBoundingVolume(cN.placement.position))


   // 3. add every axis' min and max value to a axis-array
   collisionNodes.foreach(cN => {
     cN.collision.boundingVolume match {
       case a: AABB => addAABB2AxisArray(a)
       case s: Sphere => //TODO: do something here
       case _ =>
     }
   })


   // sweep and prune collisions
   sweepAndPrune(xAxis)
   sweepAndPrune(yAxis)
   sweepAndPrune(zAxis)




   collisions ++= pairs.filter(p => {p._2 == 3}).keySet

   val end = System.currentTimeMillis()

   println("elems: "+collisionNodes.length,"collisions: "+collisions.length)
   println(collisions.toList)




   println("Time:" +(end-start))


   this
 }

 def sapOld(axis: mutable.ArrayBuffer[BBEndPoint]) {
   for(i <- 1 until axis.length) {
     val current = axis(i)
     val before = axis(i-1)

     val pair: (Identifier, Identifier) = (current.owner(), before.owner())

     if(current.isMin && !before.isMin) {
       // before MAX && current MIN
       // no collision
       if(pairs.contains(pair)) {
         pairs(pair) -= 1
       } else {
         // don't add a pair - can't remove because none is in pairs
         println("ähm2")
       }
     }
     else if(!current.isMin && before.isMin) {
       //  before MIN && current MAX
       // collision
       if(pairs.contains(pair)) {
         pairs(pair) += 1
       } else {
         pairs += pair -> 1
       }

     }
     else if(!current.isMin && !before.isMin){
       //before MAX && current MAX
       println("ähm ...")

     }
     else if(current.isMin && before.isMin){
       // current MIN && before MIN
       // nested start collision
       if(pairs.contains(pair)) {
         pairs(pair) += 1
       } else {
         pairs += pair -> 1
       }

     } else {
       println("what?!")
     }


   }
 }



 def pairHandle(pair: Pair, increase: Boolean) {

 }*/


}
