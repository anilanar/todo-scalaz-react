package tutorial.webapp.services.js

import scalaz._
import Scalaz._

import scala.scalajs.js

trait Json[T] {
  def stringify(t: T): JsError \/ String
  def parse(s: String): T
}

trait EncodeJson[T] {
  def encode(t: T): js.Any
}

trait DecodeJson[T] {
  def decode(j: js.Any): String \/ T
}

object Json {
  def stringify[A](a: A)(implicit J: Json[A]) = J.stringify(a)
  def parse[A](s: String)(implicit J: Json[A]) = J.parse(s)

  implicit val jsonJsAny = new Json[js.Any] {
    def stringify(t: js.Any): JsError \/ String =
      try {
        \/-(js.JSON.stringify(t))
      } catch {
        case e: js.JavaScriptException => -\/(JsError(e))
      }

    def parse(s: String): js.Any = js.JSON.parse(s).asInstanceOf[js.Any]
  }
}

object EncodeJson {
  def encode[A](a: A)(implicit E: EncodeJson[A]): js.Any = E.encode(a)
}

object DecodeJson {
  def decode[A](j: js.Any)(implicit D: DecodeJson[A]): String \/ A = D.decode(j)
}
