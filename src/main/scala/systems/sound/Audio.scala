package main.scala.systems.sound

import org.lwjgl.openal.AL10._
import org.lwjgl.util.WaveData
import org.lwjgl.openal._
import main.scala.io.FileIO
import main.scala.tools.DC
import main.scala.math.Vec3f
import scala.collection.mutable
import org.lwjgl.BufferUtils
import java.nio.{FloatBuffer, IntBuffer}
import javax.sound.sampled.{AudioSystem, AudioInputStream}
import main.scala.engine.GameEngine
import java.io.File

/**
 * Created by Christian Treffs
 * Date: 24.03.14 21:34
 *
 * http://relativity.net.au/gaming/java/AudioClasses.html
 */
object Audio {

  val audioBuffers = mutable.HashMap.empty[Symbol, AudioBuffer]
  val audioSources = mutable.HashMap.empty[Symbol, AudioSource]

  def init() {
    AL.create()

    if (alGetError != AL_NO_ERROR) {
      deinit()
      throw new Exception("error creating audio context")
    }

    DC.log("Audio", "initialized")
  }

  def loadWave(identifier: Symbol, file: File) {
    audioBuffers += identifier -> new AudioBuffer(file)
    DC.log(identifier + " audio buffer", "loaded from " + file.getAbsoluteFile)
  }

  def load() {
    val soundFiles = FileIO.loadAll("wav",GameEngine.soundsDir)
    soundFiles.foreach(f => {
      val nameSym = Symbol(FileIO.getName(f))
      loadWave(nameSym,f)
      createSource(nameSym,audioBuffers(nameSym))
    })
  }

  def createSource(identifier: Symbol, audioBuffer: AudioBuffer) {
    audioSources += identifier -> new AudioSource(audioBuffer)
    DC.log(identifier + " audio source", "created",3)
  }



  def getBuffer(identifier: Symbol): Option[AudioBuffer] = audioBuffers.get(identifier)

  def getSource(identifier: Symbol): Option[AudioSource] = audioSources.get(identifier)


  def deinit() {
    audioSources.values.foreach(_.deinit())
    audioBuffers.values.foreach(_.deinit())
    AL.destroy()
    DC.log("Audio", "ended")
  }
}


sealed class AudioDynamicLocation() {

  protected val _position = BufferUtils.createFloatBuffer(3).put(Array[Float](0.0f, 0.0f, 0.0f))
  protected val _velocity = BufferUtils.createFloatBuffer(3).put(Array[Float](0.0f, 0.0f, 0.0f))

  _position.flip()
  _velocity.flip()

  def position = _position

  def position_=(vec: Vec3f) {
    _position.put(0, vec.x)
    _position.put(1, vec.y)
    _position.put(2, vec.z)
  }

  def velocity = _velocity

  def velocity_=(vec: Vec3f) {
    _velocity.put(0, vec.x)
    _velocity.put(1, vec.y)
    _velocity.put(2, vec.z)
  }

}

sealed class AudioListener extends AudioDynamicLocation {
  // Buffer that holds the orientation of the listener of audio.
  // The values are the x, y and z values for direct the listener is looking followed by the orientation of up.
  // In this case looking down the negative z aixs with up along the y axis.

  protected val orientation: FloatBuffer = BufferUtils.createFloatBuffer(6).put(Array[Float](0.0f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f))


  orientation.flip()
  alListener(AL_POSITION, position)
  alListener(AL_VELOCITY, velocity)
  alListener(AL_ORIENTATION, orientation)


  // override setPosition method in AudioDynamicLocation

  override def position_=(vec: Vec3f) {
    super.position_=(vec)
    alListener(AL_POSITION, position)
  }


  // override setVelocity method in AudioDynamicLocation

  override def velocity_=(vec: Vec3f) {
    super.velocity_=(vec)
    alListener(AL_VELOCITY, velocity)
  }


  def setOrientation(x: Float, y: Float, z: Float, upX: Float, upY: Float, upZ: Float) {
    orientation.put(0, x)
    orientation.put(1, y)
    orientation.put(2, z)

    orientation.put(3, upX)
    orientation.put(4, upY)
    orientation.put(5, upZ)

    alListener(AL_ORIENTATION, orientation)
  }
}


sealed class AudioBuffer(file: File) extends AudioDynamicLocation {

  private val buffer: IntBuffer = BufferUtils.createIntBuffer(1)

  alGenBuffers(buffer)
  if (alGetError() != AL_NO_ERROR) {
    throw new Exception("error generating audio buffer for '" + file.getAbsoluteFile + "'")
  }



  val audioIn: AudioInputStream = AudioSystem.getAudioInputStream(file)
  val waveFile: WaveData = WaveData.create(audioIn)

  alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate)

  waveFile.dispose()

  if (alGetError() != AL_NO_ERROR) {
    DC.warn("check your audio file", (audioIn.getFormat, audioIn.getFrameLength))
    throw new Exception("error generating wave data '" + file.getAbsoluteFile + "' ERROR#:" + alGetError())
  }

  def id: Int = buffer.get(0)

  def deinit() {
    alDeleteBuffers(buffer)
  }

  override def toString: String = {
    "AudioBuffer file : " + file.getAbsoluteFile
  }

  override def finalize() {
    super.finalize()
    DC.log("AudioBuffer", "finalized")
    deinit()
  }

}

sealed class AudioSource(audioBuffer: AudioBuffer) extends AudioDynamicLocation {


  // Buffer that holds a reference number for a source of audio.
  private val source: IntBuffer = BufferUtils.createIntBuffer(1)
  private val buffer: AudioBuffer = audioBuffer


  alGenSources(source); // ask the audio card for a number to identify this source. Place the number in source


  if (alGetError() != AL_NO_ERROR) {
    throw new Exception("error creating audio source " + alGetError())
  }

  // setup the properties of the audio source

  alSourcei(source.get(0), AL_BUFFER, buffer.id)
  alSourcef(source.get(0), AL_PITCH, 1.0f)
  alSourcef(source.get(0), AL_GAIN, 1.0f)
  alSource(source.get(0), AL_POSITION, position)
  alSource(source.get(0), AL_VELOCITY, velocity)

  override def position_=(vec: Vec3f) {
    super.position_=(vec)
    alSource(source.get(0), AL_POSITION, position)
  }

  override def velocity_=(vec: Vec3f) {
    super.velocity_=(vec)
    alSource(source.get(0), AL_VELOCITY, velocity)
  }

  protected def audioBuffer_=(buffer: AudioBuffer) {
    alSourcei(source.get(0), AL_BUFFER, buffer.id)
  }

  def pitch_=(pitch: Float) {
    alSourcef(source.get(0), AL_PITCH, pitch)
  }

  def gain_=(gain: Float) {
    alSourcef(source.get(0), AL_GAIN, gain)
  }

  def loop(loop: Boolean = true) {
    alSourcei(source.get(0), AL_LOOPING, if (loop) AL_TRUE else AL_FALSE)
  }

  def play() = {
    //if(!isPlaying) {
      alSourcePlay(source.get(0))
    //}
  }

  def pause() = {
    //if(!isPaused) {
      alSourcePause(source.get(0))
    //}
  }

  def stop() = {
    //if(!isStopped) {
      alSourceStop(source.get(0))
    //}
  }

  def isPlaying: Boolean = alGetSourcei(source.get(0), AL_SOURCE_STATE) == AL_PLAYING

  def isPaused: Boolean = alGetSourcei(source.get(0), AL_SOURCE_STATE) == AL_PAUSED

  def isStopped: Boolean = alGetSourcei(source.get(0), AL_SOURCE_STATE) == AL_STOPPED


  def deinit() {
    alDeleteSources(source)
  }


  // override the toString method in Object

  override def toString: String = "Audio Source " + {
    if (isPlaying) "is playing" else if (isPaused) "is paused" else if (isStopped) "stopped playing"
  } + buffer

  // override the finalize method in Object

  override def finalize() {
    super.finalize()
    DC.log("AudioSource", "finalized")
    deinit()
  }


}