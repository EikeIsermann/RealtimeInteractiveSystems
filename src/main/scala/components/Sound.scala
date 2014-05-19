package main.scala.components

import main.scala.architecture.{ComponentCreator, Component}
import main.scala.tools.{DC, Identifier}
import scala.xml.Node
import main.scala.systems.sound.{Audio, AudioSource}
import main.scala.math.Vec3f
import scala.collection.mutable.ArrayBuffer

/**
 * Created by Christian Treffs
 * Date: 19.05.14 10:58
 */
object Sound extends ComponentCreator {

  override def fromXML(xml: Node): Option[Sound] = ???
}

class Sound(soundIdentifier: Seq[Symbol] = Seq()) extends Component {

  private val audioSources: ArrayBuffer[AudioSource] = ArrayBuffer.empty[AudioSource]
  soundIdentifier.foreach(+=)

  def +=(soundIdentifier: Symbol) = {
    Audio.getSource(soundIdentifier).collect{
      case as: AudioSource => audioSources += as
      case _ => DC.log("WARNING: trying to add non present AudioSource",soundIdentifier)
    }
  }
  def -=(soundIdentifier: Symbol) =  {
    Audio.getSource(soundIdentifier).collect{
      case as: AudioSource => audioSources -= as
      case _ => DC.log("WARNING: trying to remove non present AudioSource",soundIdentifier)
    }
  }

  def sources: Seq[AudioSource] = audioSources.toSeq

  def position_=(pos: Vec3f) = audioSources.foreach(_.position_=(pos))

  def velocity_=(vel: Vec3f) = audioSources.foreach(_.velocity_=(vel))

  def play() = audioSources.foreach(_.play())
  def pause() = audioSources.foreach(_.pause())
  def stop() =  audioSources.foreach(_.stop())


  override def newInstance(identifier: Identifier): Component = new Sound(soundIdentifier) //TODO: not using all comps

  override def toXML: Node = ???
}
