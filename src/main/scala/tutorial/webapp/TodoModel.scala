package tutorial.webapp.model

import scala.scalajs.js
import scalaz._
import Scalaz._
import tutorial.webapp.services.js._

sealed abstract class TodoItem {
  def title: String
  def id: Int
}
final case class Incomplete(title: String, id: Int) extends TodoItem
final case class Complete(title: String, id: Int) extends TodoItem

object TodoItem {
  implicit val encodeJsonTodoItemJsAny = new EncodeJson[TodoItem] {
    def encode(t: TodoItem): js.Any = {
      val todoItemJs = new TodoItemJs(
        t.title,
        t.id,
        t match {
          case Complete(_, _) => true
          case Incomplete(_, _) => false
        }
      )
      todoItemJs
    }
  }

  implicit val decodeJsonTodoItemJsAny = new DecodeJson[TodoItem] {
    def decode(j: js.Any): String \/ TodoItem = {
      try {
        val todoItemJs = j.asInstanceOf[TodoItemJs]
        val todoItem =
          if (todoItemJs.isComplete)
            Complete(todoItemJs.title, todoItemJs.id)
          else
            Incomplete(todoItemJs.title, todoItemJs.id)
        \/-(todoItem)
      } catch {
        case e: Throwable => -\/(e.toString)
      }
    }
  }
}

@js.native
@js.annotation.JSGlobal
private class TodoItemJs(var title: String, var id: Int, var isComplete: Boolean, var foo: TodoItemJs = null) extends js.Object

