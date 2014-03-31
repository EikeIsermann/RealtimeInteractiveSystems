package main.scala.architecture

import main.scala.tools.Identifier

/**
 * Created by Christian Treffs
 * Date: 14.03.14 17:43
 *
 * Components are simple value object.
 * The raw data for one aspect of the object, and how it interacts with the world.
 * Labels the Entity as possessing this particular aspect.
 *
 */
trait Component /*extends ObservingActor*/ {
  private val _identifier: Identifier = Identifier.create(this.getClass.getSimpleName)

  def identifier: Identifier = _identifier

  def toXML: scala.xml.Node

  override def toString: String = "[Component] "+identifier.toString
}
trait ComponentCreator {

  def fromXML(xml: scala.xml.Node): Option[Component]


  /**
   * reads the xml node where label equals the wanted value and applies the given function to get the right component
   * for the xml tag
   * @param xml the xml node
   * @param label the label to look for
   * @param func the function to be applied
   * @return the generated component
   */
  def xmlToComp[T <: Component](xml: scala.xml.Node, label: String, func: scala.xml.Node => Option[T]): Option[T] = {
    val x = (xml \ label).filter(_.label.equals(label))
    if(x.length > 1) {
      throw new IllegalArgumentException("multiple components defined for component '"+label+"'")
    }
    var ret: Option[T] = None
    x.foreach(c => ret = func(c))
    ret
  }
}
