package main.scala.tools

/**
 * Created by Christian Treffs
 * Date: 21.01.14 16:58
 */
object DC {

  /**
   * the debug level between 0 (lowest priority) and 3 (highest priority)
   */
  private var _debugLevel = 0

  /**
   * getter, setter and validator for debug level
   */
  def debugLevel = _debugLevel
  def debugLevel_= (level: Int): Unit = {
    if(isValidDebugLevel(level)) {
      _debugLevel = level
    }
  }
  def isValidDebugLevel(level: Int): Boolean = {
    if(level >= 0 && level <= 3) {
      true
    } else {
      throw new IllegalArgumentException("Debug Level must be between 0 (low priority) and 3 (high priority)")
      false
    }
  }


  /**
   * the log method to log a message
   * @param msg the message to be shown
   * @param arg the argument to show
   * @param level the debug level between 0 and 3
   * @return "["+msg+"] --> "+arg
   */
  def log(msg: String, arg: Any = "", level: Int = 0): String = {
    var ret = ""
    if(isValidDebugLevel(level)) {
      if(debugLevel <= level) {
        ret = "["+msg+"] --> "+arg
        println(ret)
      }
    }
    ret
  }
}
