package test

import main.scala.architecture._
import scala.xml.{NodeSeq, Elem, NodeBuffer}
import scala.xml

/**
 * Created by Christian Treffs
 * Date: 28.03.14 17:46
 */
object XMLInOutTest {

  def main(args: Array[String]) {

    Loader.load(xml)
  }


  val xml = {<entity>
              <comp1>zweimer</comp1>
              <comp>
                <a>a</a>
                <b id="12"></b>
                <c id="4949" />
              </comp>
            </entity>}


}

object Loader {
  def load(xml: Elem) {
    Comp.fromXML(xml)





  }
}

object Comp extends ComponentCreator {

  override def fromXML(xml: scala.xml.Node): Component = {
    xmlToComp(xml, "comp", n => {
      val a = n \ "a"
      val b = n \ "b"
      val c = n \ "c"
      println(a.text, b \ "@id", c \ "@id")

      return new Comp
    })
  }
}

class Comp extends Component {
  override def toXML: scala.xml.Node = <comp>test</comp>

}