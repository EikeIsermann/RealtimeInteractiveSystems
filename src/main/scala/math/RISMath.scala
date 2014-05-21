package main.scala.math

/**
 * User: uni
 * Date: 21.05.14
 * Time: 10:17
 * This is a RIS Project class
 */
object RISMath {

  def DirFromRot(rot: Vec3f, forward: Boolean  = true): Vec3f = {

    var xRad = -(math.sin(math.toRadians(-rot.y)).toFloat)
    var zRad = math.cos(math.toRadians(-rot.y)).toFloat
    var yRad = math.sin(math.toRadians(rot.x)).toFloat
    var retVal = Vec3f(xRad * (1-Math.abs(yRad)), yRad, -(zRad * (1-Math.abs(yRad))))
    if(!forward){
      retVal = retVal * Vec3f(-1,0,-1)
    }
    retVal
  }
}
