package test

import main.scala.architecture._
import scala.xml.Elem
import main.scala.components._
import scala.xml

/**
 * Created by Christian Treffs
 * Date: 28.03.14 17:46                                    ⁄
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

              <placement>
                <position x="1" y="2" z="3" />
                <rotation angleX="10" angleY="20" angleZ="30" />
                <scale x="100" y="200" z="300" />
              </placement>
              <motion>
                <direction x="123" y="234" z="345" />
                <velocity x="098" y="987" z="876" />
                <friction>194857.345</friction>
              </motion>
              <display>
                <meshId>eimer</meshId>
                <shaderId>zweimer</shaderId>
              </display>
            </entity>}


}

object Loader {
  def load(xml: Elem) {
    val c = Comp.fromXML(xml)

    val p = Placement.fromXML(xml)
    val m = Motion.fromXML(xml)
    val d = Display.fromXML(xml)



  }
}

object Comp extends ComponentCreator {

  override def fromXML(xml1: xml.Node): Option[Comp] = xmlToComp[Comp](xml1, "comp", n => {
      val a = n \ "a"
      val b = n \ "b"
      val c = n \ "c"
      println(a.text, b \ "@id", c \ "@id")

      Some(new Comp())
    }
  )

}

class Comp extends Component {
  override def toXML: scala.xml.Node = <comp>test</comp>

}