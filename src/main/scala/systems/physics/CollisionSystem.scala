package main.scala.systems.physics

import main.scala.architecture._
import main.scala.tools.{Sphere, BBEndPoint, AABB, Identifier}
import main.scala.components._
import scala.collection.mutable
import main.scala.nodes.CollisionNode
import main.scala.math.{Mat4f, Vec3f}
import main.scala.engine.GameEngine
import main.scala.systems.gfx.Shader
import main.scala.entities.Entity
import main.scala.event.{PlayerLeftView, PlayerEnteredView, EventDispatcher}
import main.scala.systems.ai.aiStates._

/**
 * Created by Christian Treffs
 * Date: 06.05.14 15:55
 */
case class Pair(a: Identifier, b: Identifier)

class CollisionSystem(simSpeed: Int) extends IntervalProcessingSystem {

  override var acc: Float = 0
  override val interval: Float = 1f/simSpeed.toFloat


  private var xAxis = mutable.ArrayBuffer[BBEndPoint]()
  private var yAxis = mutable.ArrayBuffer[BBEndPoint]()
  private var zAxis = mutable.ArrayBuffer[BBEndPoint]()
  private val pairVecs = mutable.HashMap[(Collision, Collision), PairVec]()
  private val pairs = mutable.HashMap[(Collision, Collision), Int]()
  private val collisions = mutable.ArrayBuffer[(Collision, Collision)]()
  private val activeCollisions = mutable.ArrayBuffer[(Collision, Collision)]() //DO NOT CLEAR



  override var node: Class[_ <: Node] = classOf[CollisionNode]
  override var priority = 0




  def init(): System = {
    println(this,"interval:"+interval)

    this
  }

  override def begin(): Unit = {
    xAxis.clear()
    yAxis.clear()
    zAxis.clear()
    pairs.clear()
    pairVecs.clear()
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


          if(GameEngine.showCollisionBox) {
            collision.boundingVolume.draw(Shader.get('default), Mat4f.identity, ctx.viewMatrix, ctx.fieldOfView,ctx.aspect,ctx.nearPlane,ctx.farPlane)
          }

          collision.boundingVolume match {
            case a: AABB => addAABB2AxisArray(a)
            case s: Sphere => //TODO:
          }
        case _ => throw new IllegalArgumentException("not a CollisionNode")
      }



  }

  override def end(): Unit = {

    // sweep and prune collisions
    sweepAndPrune(xAxis, 0)
    sweepAndPrune(yAxis, 1)
    sweepAndPrune(zAxis, 2)

    // keep the ones that are collisions
    collisions ++= pairs.filter(p => {p._2 == 3}).keySet



    val endedCollisions = activeCollisions.filterNot(collisions.contains)
    endedCollisions.foreach(handleEndedCollision)

    val stillActiveCollisions = activeCollisions.filter(collisions.contains)
    stillActiveCollisions.foreach(handleActiveCollision)

    val newCollisions = collisions.filterNot(stillActiveCollisions.contains)
    newCollisions.foreach(handleNewCollision) //handle them

    activeCollisions.clear()
    activeCollisions ++= collisions


  }


  private def handleNewCollision(pair: (Collision,Collision)) {



    val c1: Collision = pair._1
    val c2: Collision = pair._2

    val vecPair = pairVecs(pair)
    val colPoint = collisionPoint(vecPair.vec1,vecPair.vec2)

    val e1 = GameEngine.entities(c1.owner.toString)
    val e2 = GameEngine.entities(c2.owner.toString)

    var skip: Boolean = false
    if(e1.has(classOf[Children])) {
       skip = e1.getComponent(classOf[Children]).children.contains(e2)
    }
    if(e2.has(classOf[Children])) {
      skip = e2.getComponent(classOf[Children]).children.contains(e1)
    }

    if(skip) return




    // AI sees player
    if(either(e1,e2,classOf[GunAI],classOf[GunControl])) {
      whoHas(e1,e2,classOf[GunAI]).getComponent(classOf[GunAI]).state = new gunTargetAcquired(whoHas(e1,e2,classOf[GunControl]))
    }

    //println("Collision New",pair)
    //println("Collision: "+e1.identifier+" & "+e2.identifier," collide @ "+colPoint.inline)

    /*
    val e1Phys = e1.getIfPresent(classOf[Physics]).get
    val e2Phys = e2.getIfPresent(classOf[Physics]).get

    //println(e1Phys.velocity.inline,e2Phys.velocity.inline,e1Phys.acceleration.inline,e2Phys.acceleration.inline)

    val a1 = e1Phys.velocity
    val a2 = e2Phys.velocity


    //println("VelACC:"+e1Phys.velocity.length(),e1Phys.acceleration.length(),e2Phys.velocity.length(),e2Phys.acceleration.length())


    //println(a1.inline,a2.inline)

    val m1 = e1Phys.mass
    val m2 = e2Phys.mass


    val f1: Vec3f = (-100f*a2*a2*m2)/m1
    val f2: Vec3f = (-100f*a1*a1*m1)/m2


*/
    //println("FORCE: "+f1.inline,f2.inline)

    /*
    e1Phys.velocity = Vec3f(0,0,0)
    e2Phys.velocity = Vec3f(0,0,0)
    e1Phys.acceleration = Vec3f(0,0,0)
    e2Phys.acceleration = Vec3f(0,0,0)

    e1.getIfPresent(classOf[Physics]).get.addForce(f1)
    e2.getIfPresent(classOf[Physics]).get.addForce(f2)

    */

    // play collision sound if there is a sound component with collision
    e1.getIfPresent(classOf[Sound]).map(_.playList += 'collision)
    e2.getIfPresent(classOf[Sound]).map(_.playList += 'collision)

    e1.getIfPresent(classOf[Health]).map(_.damage(10))
    e2.getIfPresent(classOf[Health]).map(_.damage(10))

    //e1.destroy()
    //e2.destroy()
  }

  private def handleActiveCollision(pair: (Collision,Collision)) {

  }

  private def handleEndedCollision(pair: (Collision,Collision)) {
    //println("CollisionEnded",pair)
    val c1: Collision = pair._1
    val c2: Collision = pair._2
    val e1 = GameEngine.entities(c1.owner.toString)
    val e2 = GameEngine.entities(c2.owner.toString)

    // remove bullet after collision
    //e1.hasAndThen(classOf[Projectile], e => {e.destroy()})
    //e2.hasAndThen(classOf[Projectile], e => {e.destroy()})





    if(either(e1,e2,classOf[GunAI],classOf[GunControl])) {
      println(e1,e2,"has left view")
      whoHas(e1,e2,classOf[GunAI]).getIfPresent(classOf[GunAI]).map(_.state = new gunSearching())
    }

  }


  def deinit(): Unit = {}

  private def whoHas(e1: Entity, e2: Entity, c: Class[_ <: Component]): Entity = {
    if(e1.has(c)) e1
    else e2
  }

  private def either(e1: Entity, e2: Entity, c1: Class[_ <: Component], c2: Class[_ <: Component]): Boolean = (e1.has(c1) && e2.has(c2)) || (e1.has(c2) && e2.has(c1))


  private def collisionPoint(v1: Vec3f,v2: Vec3f): Vec3f = 0.5f*(v2+v1)

  private def addAABB2AxisArray(aabb: AABB) {
    xAxis ++= aabb.interval(0)
    yAxis ++= aabb.interval(1)
    zAxis ++= aabb.interval(2)
  }

  case class PairVec(vec: (Float,Float),axis: Int) {
    val values: Array[Array[Float]] = Array.ofDim[Float](3,2)
    add(vec,axis)
    def vec1: Vec3f = Vec3f(values(0)(0),values(1)(0),values(2)(0))
    def vec2: Vec3f = Vec3f(values(0)(1),values(1)(1),values(2)(1))
    def add(vec: (Float,Float), axis: Int) {

      values(axis)(0) = vec._1
      values(axis)(1) = vec._2
    }
  }

  private def sweepAndPrune(axis: mutable.ArrayBuffer[BBEndPoint], axisHint: Int) {
    for (j <- 1 until axis.size) {
      val current: BBEndPoint = axis(j)
      var i = j - 1
      while (i >= 0 && axis(i).value > current.value) {
        val before: BBEndPoint = axis(i)
        val pair = (current.owner().asInstanceOf[Collision], before.owner().asInstanceOf[Collision])
        if (current.isMin && !before.isMin) {
          pairs.contains(pair) match {
            case true =>
              pairVecs(pair).add((before.value,current.value),axisHint)
              pairs(pair) += 1
            case false =>
              pairVecs += pair -> PairVec((before.value,current.value),axisHint)
              pairs += pair -> 1
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
}
