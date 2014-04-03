package test.physics

import main.scala.math.Vec3f
import main.scala.tools.phy

/**
 * Created by Christian Treffs
 * Date: 02.04.14 10:06
 */
object PhysicsTest {
  // DON'T USE EULER INTEGRATION BECAUSE IT'S INACCURATE OVER NON CONSTANT TIME STEPS AND OVER SMALL TIME STEPS!
  // USE RK4 Runge Kutta order 4!
  // http://gafferongames.com/game-physics
  /*

  var c = 0

  // Semi-fixed timestep
  val simulationsPerSecond: Int = 200

  var t: Double = 0
  var dt: Double = 0.001//1/simulationsPerSecond

  val maxFrameTime: Double = 0.25

  var currentTime = phy.timeInSeconds()
  var accumulator: Double = 0.0

  var previousState: State = State(Vec3f(0, 0, 0), Vec3f(1, 0, 0))
  val currentState: State = State(Vec3f(0,0,0), Vec3f(1,0,0)) // comes from the components

  def main(args: Array[String]) {



    while(c < 100000) {

      val newTime: Double = phy.timeInSeconds()
      var frameTime: Double = newTime - currentTime
      if(frameTime > maxFrameTime) {
        frameTime = maxFrameTime  // max frame time to avoid spiral of death
      }
      currentTime = newTime

      accumulator += frameTime


      while(accumulator >= dt)  {
        println(t.formatted("%f"),"SIMULATE", accumulator)
        previousState = currentState
        integrate(currentState, t, dt)
        t += dt
        accumulator -= dt
      }

      val alpha: Double = accumulator / dt
      val state: State = currentState*alpha + previousState * (1.0-alpha)

      // render (state)

      c += 1
    }


  }


  def evaluate(initial: State, d: Derivative, t: Double, dt: Double): Derivative = {

    val state = State(
      initial.v + d.dx*dt.toFloat,
      initial.v + d.dv*dt.toFloat
    )

    val output = Derivative(
      state.v,
      acceleration(state,t+dt)
    )

    //println("ev", t, dt, state.x.inline, state.v.inline, output.dx.inline, output.dv.inline)
    output



  }

  def acceleration(state: State, t: Double): Vec3f = {
    val k: Float =  10
    val b: Float = 1
    -k * state.x - b * state.v
  }

  def integrate(state: State, t: Double, dt: Double) {
    val a: Derivative = evaluate(state, Derivative(), t, 0.0f)
    val b: Derivative = evaluate(state, a, t, dt*0.5f)
    val c: Derivative = evaluate(state, b, t, dt*0.5f)
    val d: Derivative = evaluate(state, c, t, dt)

    val dxdt: Vec3f = 1.0f/6.0f * (a.dx + 2.0f*(b.dx + c.dx) + d.dx)
    val dvdt: Vec3f = 1.0f/6.0f * (a.dv + 2.0f*(b.dv + c.dv) + d.dv)

/*    state.x = state.x + dxdt * dt.toFloat
    state.v = state.v + dvdt * dt.toFloat
  */
    //println(t, state.x.inline, state.v.inline)

  }

  case class State(private val pos1: Vec3f, private val momentum1: Vec3f, private val mass1: Float) {

    private var _position: Vec3f = pos1
    private var _momentum: Vec3f = momentum1
    private var _mass: Float = mass1
    private var _velocity: Vec3f = Vec3f()
    private var _inverseMass: Float = 0

    calcInverseMass()
    calcVelocity()

    def position: Vec3f = _position
    def position_=(p: Vec3f) = {
      _position = p
      calcInverseMass()
      calcVelocity()
      _position
    }

    def momentum: Vec3f = _momentum
    def momentum_=(m: Vec3f) = {
      _momentum = m
      calcInverseMass()
      calcVelocity()
      _momentum
    }

    def mass: Vec3f = _mass
    def mass_=(m: Float) = {
      _mass = m
      calcInverseMass()
      calcVelocity()
      _mass
    }


    def velocity: Vec3f = _velocity
    def inverseMass: Float = _inverseMass


    def calcVelocity() {
      _velocity = _momentum * _inverseMass
    }

    def calcInverseMass() {
      _inverseMass = 1f/_mass
    }

  }

  case class Derivative(var dx: Vec3f = Vec3f(), var dv: Vec3f = Vec3f())
     */
}
