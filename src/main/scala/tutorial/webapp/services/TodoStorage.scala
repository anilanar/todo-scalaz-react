package tutorial.webapp.services

import scalaz.Scalaz._
import scalaz._
import tutorial.webapp.model._

import scala.scalajs.js

object TodoStorage {
  final class TodoLocalStorage[F[_]: Monad](implicit L: LocalStorage[F]) {
    def add(item: TodoItem) = L.put(
      item.id.shows,
      Json.stringify(item)
    )
    def remove(item: TodoItem) = L.remove(item.id.shows)
  }

}

@js.native
@js.annotation.JSGlobal
private final class Json extends js.Object {
  var id: Int
  var title: String
  var isComplete: Boolean
}

private object Json {
  def deserialize(json: Json) = json.isComplete match {
    case true => Complete(json.title, json.id)
    case false => Incomplete(json.title, json.id)
  }
  def stringify(v: TodoItem): String = js.JSON.stringify(
    new Json(v.id, v.title, v match {
      case Complete(_, _) => true
      case Incomplete(_, _) => false
    })
  )
  def parse(s: String): Either[String, TodoItem] = try {
    Right(deserialize(js.JSON.parse(s).asInstanceOf[Json]))
  } catch {
    case ex: Throwable =>  Left(ex.toString)
  }
}

