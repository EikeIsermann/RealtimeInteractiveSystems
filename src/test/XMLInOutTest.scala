package test

import main.scala.architecture._
import scala.xml.Elem
import main.scala.components.Placement
import scala.xml
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

              <placement>
                <position x="1" y="2" z="3" />
                <rotation angleX="10" angleY="20" angleZ="30" />
                <scale x="100" y="200" z="300" />
              </placement>
            </entity>}


}

object Loader {
  def load(xml: Elem) {
    Comp.fromXML(xml)

    val p = Placement.fromXML(xml)


    println(p.position.inline,p.rotation.inline, p.scale.inline)



    println(p.toXML)


  }
}

object Comp extends ComponentCreator {

  override def fromXML(xml1: xml.Node): Comp = xmlToComp[Comp](xml1, "comp", n => {
      val a = n \ "a"
      val b = n \ "b"
      val c = n \ "c"
      println(a.text, b \ "@id", c \ "@id")

      new Comp()
    }
  )

}

class Comp extends Component {
  override def toXML: scala.xml.Node = <comp>test</comp>

}