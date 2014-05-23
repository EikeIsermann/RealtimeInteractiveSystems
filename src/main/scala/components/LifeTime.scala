package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import scala.xml.Node
import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 21.03.14 00:47
 */
object LifeTime extends ComponentCreator {
  override def fromXML(xml: Node): Option[LifeTime] = xmlToComp[LifeTime](xml, "lifeTime", n => {

    val lT: Long = (n \ "time").text.toLong
    val l: Long = (n \ "remaining").text.toLong


    val i = new LifeTime(lT, l)
    println(i.lifeTime)
    Some(i)
  })
}

case class LifeTime(_lifeTime: Long = 0, _timeLeft: Long = 0) extends Component{

  var lifeTime: Long = _lifeTime
  var timeLeft: Long = _timeLeft
  var initTime1: Long = System.currentTimeMillis()

  def -=(t: Long) = {timeLeft = timeLeft-t}
  def +=(t: Long) = {if(timeLeft <= lifeTime) timeLeft = timeLeft+t}


  override def newInstance(i:Identifier): Component = new LifeTime(lifeTime,timeLeft)

  override def toXML: Node = {
  <lifeTime>
    <time>{lifeTime.toString}</time>
    <remaining>{timeLeft.toString}</remaining>
  </lifeTime>}

}
