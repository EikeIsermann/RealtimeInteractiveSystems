package main.scala.math

/**
 * User: uni
 * Date: 21.05.14
 * Time: 10:17
 * This is a RIS Project class
 */
object RISMath {

  def DirFromRot(rot: Vec3f, forward: Boolean  = true): Vec3f = {

    var xRad = math.sin(math.toRadians(-rot.y)).toFloat
    var zRad = math.cos(math.toRadians(-rot.y)).toFloat
    var  yRad = math.sin(math.toRadians(rot.x)).toFloat

    var x =  xRad * math.cos(math.toRadians((rot.x))).toFloat
    var z =  zRad * math.cos(math.toRadians((rot.x))).toFloat
    var y =  Math.abs(yRad)


    if(forward) z = z * -1
    else x = x * -1

    var retVal = Vec3f(x, y, z)

    retVal
  }
}
