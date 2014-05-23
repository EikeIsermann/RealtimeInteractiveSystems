package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import main.scala.tools.Identifier
import scala.xml.Node
import main.scala.systems.sound.{Audio, AudioSource}
import main.scala.math.Vec3f
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 19.05.14 10:58
 */
object Sound extends ComponentCreator {

  override def fromXML(xml: Node): Option[Sound] = {
    xmlToComp[Sound](xml, "sounds", n => {

      val snd = mutable.HashMap.empty[Symbol,Symbol]

      (n \ "sound").foreach(s => {
         val soundType: Symbol = Symbol((s \ "@type").text.trim)
         val soundIdentifier: Symbol = Symbol(s.text.trim)

         snd += soundType -> soundIdentifier
       })

      Some(new Sound(snd))
    })
  }
}

case class Sound(soundIdentifier: mutable.HashMap[Symbol,Symbol] = mutable.HashMap()) extends Component {

  var playList: ArrayBuffer[Symbol] = ArrayBuffer.empty[Symbol]

  def get(sId: Symbol): AudioSource = Audio.getSource(sId).get

  def position_=(pos: Vec3f) = playList.foreach(get(_).position_=(pos))

  def velocity_=(vel: Vec3f) = playList.foreach(get(_).velocity_=(vel))

  def loop(sT: Symbol) = get(sT).loop(true)
  def play(sT: Symbol) = if(sT.name == "ost" && get(sT).isStopped) {get(sT).play(); get(sT).loop(true)} else get(sT).play()
  def play() = playList.foreach(get(_).play())
  def pause(sT: Symbol) = get(sT).pause()
  def pause() = playList.foreach(get(_).pause())
  def stop(sT: Symbol) = get(sT).stop()
  def stop() =  playList.foreach(get(_).stop())


  override def newInstance(identifier: Identifier): Component = new Sound(soundIdentifier) //TODO: not using all comps

  override def toXML: Node = {
    <sounds>
      {soundIdentifier.map(a => <sound type={a._1.name}>{a._2.name}</sound>)}
    </sounds>
  }
}
