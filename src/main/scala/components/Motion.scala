package main.scala.components

/**
 * Created by Eike
 * 23.03.14.
 */

import main.scala.math.Vec3f
import scala.xml.Node
import main.scala.architecture.{ComponentCreator, Component}


object Motion extends ComponentCreator {
  override def fromXML(xml: Node): Option[Motion] = xmlToComp[Motion](xml, "motion", n => {
    val dir = n \ "direction"
    val vel = n \ "velocity"
    val fri: Float = (n \ "friction").text.toString.toFloat

    val dirVec = Vec3f((dir \ "@x").text.toFloat,(dir \ "@y").text.toFloat,(dir \ "@z").text.toFloat)
    val velVec = Vec3f((vel \ "@x").text.toFloat,(vel \ "@y").text.toFloat,(vel \ "@z").text.toFloat)
    //TODO: adjust
    Some(new Motion(dirVec,velVec,fri))
  })
}

class Motion(direction1: Vec3f, velocity1: Vec3f, friction1: Float) extends Component {
  def this() = this(Vec3f(0,0,0),Vec3f(0,0,0), 1f)

  private var _direction: Vec3f = direction1
  private var _velocity: Vec3f = velocity1
  private var _friction: Float = friction1

  def direction: Vec3f = _direction
  def direction_=(v: Vec3f) = _direction = v

  def velocity: Vec3f = _velocity
  def velocity_=(v: Vec3f) = _velocity = v

  //TODO: needed?
  def friction: Float = _friction
  def friction_=(f:Float) = _friction = f

  //TODO: acceleration?
  override def toXML: Node = {
    <motion>
      <direction x={direction.x.toString} y={direction.y.toString} z={direction.z.toString} />
      <velocity x={velocity.x.toString} y={velocity.y.toString} z={velocity.z.toString} />
      <friction>{friction.toString}</friction>
    </motion>
  }
}
