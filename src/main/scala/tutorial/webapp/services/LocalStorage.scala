package tutorial.webapp.services

import org.scalajs.dom.ext.{LocalStorage => L}
import scalaz._
import Scalaz._
import Maybe._
import scalaz.effect._

object LocalStorage {

  trait LocalStorage[F[_]] {
    def get(key: String): F[Maybe[String]]
    def put(key: String, data: String): F[Unit]
    def mod(key: String, f: String => String): F[Unit]
    def remove(key: String): F[Unit]
  }

  implicit val localStorageIO = new LocalStorage[IO] {
    def get(key: String) = IO { L(key).toMaybe }
    def put(key: String, data: String) = IO { L.update(key, data) }
    def mod(key: String, f: String => String) = get(key).flatMap(maybe => maybe match {
      case Just(v) => put(key, f(v))
      case Empty() => IO.ioUnit
    })
    def remove(key: String) = IO { L.remove(key) }
  }
}
